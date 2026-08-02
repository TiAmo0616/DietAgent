package com.diet.service.meal;

import com.diet.enums.SourceMode;
import com.diet.mapper.MealMapper;
import com.diet.model.MealItemRow;
import com.diet.service.slot.SlotOptionService;
import com.diet.util.JsonService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MealServiceAccessTest {

    @Test
    void personalDetailUsesTrustedUserId() {
        MealMapper mapper = mock(MealMapper.class);
        MealService service = new MealService(mapper, mock(SlotOptionService.class), mock(JsonService.class));

        service.findAccessibleMeal(7L, SourceMode.PERSONAL, 99L);

        verify(mapper).findAccessibleById(99L, SourceMode.PERSONAL, 7L);
    }
}
