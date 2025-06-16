package api.controller;

import api.dto.MenuDTO;
import api.dto.UpdateMenuResponseDTO;
import api.exception.InvalidDishId;
import api.service.MenuService;
import api.utils.MenuUpdateDate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/menu")
@Tag(name="menu", description = "get menu info or update menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     *
     * @param date - date format is handled by Params, so no custom exception
     * @return menu
     */
    @GetMapping
    @Operation(description = "get menu by date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get menu of the day"),
            @ApiResponse(responseCode = "400", description = "invalid date format. Check example", content = @Content),
    })
    public MenuDTO getMenu(
            @Parameter(description = "date of menu. If not present, it uses current date.", example = "24-05-2025")
            @RequestParam(name = "date", required = false) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return menuService.getMenu(date);
    }

    /**
     * If the request is made BEFORE 10am will be changed the today menu, otherwise the one of the next day
     * @param dishIds list of dish id to put in menu
     */
    @PutMapping
    @Operation(summary = "update menu", description = "update menu using list of dishes. If it's later then 10a.m., this call will modify menu of following day, otherwise today's menu will be modified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "menu updated"),
            @ApiResponse(responseCode = "404", description = "dish id not found", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "dishes' ids list to update menu",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(type = "integer", example = "1"))
            )
    )
    public UpdateMenuResponseDTO updateMenu(@RequestBody List<Integer> dishIds){
        LocalDate date = MenuUpdateDate.get();
        menuService.updateMenu(date, dishIds);
        if (date == LocalDate.now().plusDays(1)) {
            menuService.notifyMenuChanges(date);
        }
        return new UpdateMenuResponseDTO(date);
    }
}
