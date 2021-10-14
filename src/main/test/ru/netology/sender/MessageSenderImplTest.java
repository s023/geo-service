package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.Map;
import java.util.stream.Stream;

class MessageSenderImplTest {

    @ParameterizedTest
    @MethodSource("factory")
    void send(String ipAddress, String expected) {
        final String IP_ADDRESS_HEADER = "x-real-ip";
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(ipAddress)).thenCallRealMethod();


        LocalizationService localizationService = Mockito.spy(LocalizationServiceImpl.class);
        Location location = geoService.byIp(ipAddress);
        Mockito.when(localizationService.locale(location.getCountry())).thenCallRealMethod();

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        String result = messageSender.send(Map.of(IP_ADDRESS_HEADER, ipAddress));

        Assertions.assertEquals(expected, result);
    }

    public static Stream<Arguments> factory (){
        return Stream.of(
                Arguments.of("172.0.32.11", "Добро пожаловать"),
                Arguments.of("172.00.00.00", "Добро пожаловать"),
                Arguments.of("96.44.183.149", "Welcome"),
                Arguments.of("96.11.111.111", "Welcome")
        );
    }
}