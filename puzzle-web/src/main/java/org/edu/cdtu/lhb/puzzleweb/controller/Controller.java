package org.edu.cdtu.lhb.puzzleweb.controller;

import org.edu.cdtu.lhb.puzzleutil.util.PuzzleUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class Controller {
    @PostMapping("auto_complete")
    public List<Integer> autoComplete(@RequestBody Map paramsMap) {
        List<Integer> steps = null;
        try {
            int row = (int) paramsMap.get("row");
            int col = (int) paramsMap.get("col");
            String currStatus = (String) paramsMap.get("currStatus");
            String finalStatus = (String) paramsMap.get("finalStatus");
            steps = PuzzleUtil.searchSteps(currStatus, finalStatus, row, col);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return steps;
    }
}
