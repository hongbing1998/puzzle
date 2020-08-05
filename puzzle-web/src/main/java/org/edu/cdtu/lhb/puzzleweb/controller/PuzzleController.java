package org.edu.cdtu.lhb.puzzleweb.controller;

import org.edu.cdtu.lhb.puzzleutil.Intelligetor;
import org.edu.cdtu.lhb.puzzleutil.PuzzleUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PuzzleController {
    @PostMapping("auto_complete")
    public List<Integer> autoComplete(@RequestBody Map<String, Object> paramsMap) {
        String currStr = (String) paramsMap.get("currStr");
        String rightStr = PuzzleUtil.getRightStr((int) Math.sqrt(currStr.length()));
        Intelligetor intelligetor = new Intelligetor(currStr, rightStr);
        return intelligetor.searchSteps();
    }
}
