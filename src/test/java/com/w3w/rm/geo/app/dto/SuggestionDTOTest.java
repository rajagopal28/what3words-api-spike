package com.w3w.rm.geo.app.dto;

import com.what3words.javawrapper.response.Suggestion;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class SuggestionDTOTest {

    @Test
    public void testSuggestionDTOConvertFromSuggestion() {
        Suggestion suggestion = Mockito.mock(Suggestion.class);
        String country = "country1";
        Mockito.when(suggestion.getCountry()).thenReturn(country);
        String nearbyPlace = "nearby place1";
        Mockito.when(suggestion.getNearestPlace()).thenReturn(nearbyPlace);
        String words = "nearby.place.word1";
        Mockito.when(suggestion.getWords()).thenReturn(words);

        SuggestionDTO from = SuggestionDTO.from(suggestion);
        Assert.assertNotNull(from);
        Assert.assertEquals(country, from.getCountry());
        Assert.assertEquals(words, from.getWords());
        Assert.assertEquals(nearbyPlace, from.getNearestPlace());
    }

}