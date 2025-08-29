package api.controller;

import api.dto.DishDTO;
import api.dto.ExceptionDTO;
import api.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@Tag(name="Dish", description = "Dish infos")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/{id}")
    @ResponseBody
    @Operation(description = "Get dish by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "dish found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DishDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "id not found", content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public DishDTO getDish(
            @Parameter(description = "id of dish", required = true, example = "1")
            @PathVariable("id") Integer id){
        //check if id exists
        dishService.checkId(id);
        return dishService.getDish(id);
    }

    @GetMapping
    @ResponseBody
    @Operation(description = "get all dishes of restaurant")
    @ApiResponse(responseCode = "200", description = "Returns all dishes",
    content = @Content(mediaType = "application.json"))
    public List<DishDTO> getAllDishes() {
        return dishService.getAll();
    }
}
