package org.edu.cdtu.lhb.puzzleweb.controller;

import org.edu.cdtu.lhb.puzzleutil.Intelligetor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 李红兵
 */
@RestController
public class PuzzleController {
    @PostMapping("auto_complete")
    public List<Integer> autoComplete(@RequestBody Map<String, Object> paramsMap) {
        String currStr = (String) paramsMap.get("currStr");
        Intelligetor intelligetor = new Intelligetor(currStr);
        return intelligetor.searchSteps();
    }
}
