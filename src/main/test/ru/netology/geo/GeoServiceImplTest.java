package ru.netology.geo;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.stream.Stream;

class GeoServiceImplTest {
    GeoService geoService;

    @BeforeEach
    void setUp() {
        geoService = Mockito.mock(GeoServiceImpl.class);
    }

    @ParameterizedTest
    @MethodSource("factory")
    void byIp(String ipAddress, Location expectedLocation) {


        Mockito.when(geoService.byIp(ipAddress)).thenCallRealMethod();
        Location location = geoService.byIp(ipAddress);

        Assertions.assertEquals(expectedLocation.getCountry(), location.getCountry());
        MatcherAssert.assertThat(expectedLocation, Matchers.equalTo(expectedLocation));
    }

    @Test
    void byCoordinates() {
        Mockito.when(geoService.byCoordinates(25.21,55.23)).thenCallRealMethod();
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            geoService.byCoordinates(25.21,55.23);
        });
        MatcherAssert.assertThat(e, Matchers.is(Matchers.notNullValue()));
        Assertions.assertEquals("Not implemented", e.getMessage());
    }

    public static Stream<Object> factory (){
        return Stream.of(
                Arguments.of("172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("172.00.00.00", new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("96.11.111.111", new Location("New York", Country.USA, null,  0))
        );
    }
}