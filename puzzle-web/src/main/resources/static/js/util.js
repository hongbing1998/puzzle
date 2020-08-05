// 定义一些频繁使用的全局变量
let stepsBox = $(".steps-box>text"), adviceBox = $(".advice-box");
let autoBtn = $("#auto-btn"), hintImg = $(".hint-img");
let gridsNode = null, box = $(".grid-box");// 获取盒子结点;
let mCount = $(".minutes-count"), sCount = $(".seconds-count");
let hintBtn = $("#hint-btn"), processBar = $(".process-bar");
let imgSrc = hintImg.css("backgroundImage");// 存储初始图片路径，用于重置
let util = {
    getFinalStatus: function (row, col) {
        return "0123456789:;<=>?@ABCDEFGHIJKLMNOPQR".substring(0, row * col - 1) + '\u0000';
    },
    createStatus: function (status, row, col) {
        let exchageTime = Math.round(8 + Math.random() * row * col) * 2;// 交换次数（保证是偶数）
        for (let i = 0; i < exchageTime; i++) {
            let index1 = 0, index2 = 0;
            while (index1 === index2) {
                index1 = Math.round(Math.random() * (row * col - 2));
                index2 = Math.round(Math.random() * (row * col - 2));
            }
            // 生成两个不相等的随机下标（不包含最后一个）
            let char1 = status.charAt(index1);
            let char2 = status.charAt(index2);
            status = status.replace(char1, '*').replace(char2, char1).replace('*', char2);
        }
        return status;
    },
    selectImg: function (selector) {
        let imgFile = selector.files[0];
        if (imgFile === undefined) return;
        let URL = window.URL || window.webkitURL;
        let imgUrl = URL.createObjectURL(imgFile);
        $(".hint-img,.grid").css("background-image", "url(" + imgUrl + ")");
        puzzle.init(puzzle.row, puzzle.col);
    },
    disableOperate: function () {
        for (let i = 0; i < arguments.length; i++) {
            arguments[i].attr("disabled", true);
        }
    },
    enableOperate: function () {
        for (let i = 0; i < arguments.length; i++) {
            arguments[i].removeAttr("disabled")
        }
    },
    isPC: function () {
        let mobile = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i);
        return mobile === null;
    },
    showProcess: function () {
        processBar.css("width", 0).animate({width: "100%"}, 15000, "linear");
    },
    advice: function (content) {
        adviceBox.text(content);
    },
    update: function (isAutoMove) {
        stepsBox.text(isAutoMove ? --puzzle.steps : ++puzzle.steps);// 更新步数显示
        if (isAutoMove) processBar.css("width", 100 * puzzle.steps / puzzle.totolSteps + "%");// 更新进度条显示
    }
};

if (util.isPC()) {
    hintBtn.on("mousedown", function () {
        hintImg.css("height", "100%");
    }).on("mouseup", function () {
        hintImg.css("height", 0)
    }).on("mouseleave", function () {
        hintImg.css("height", 0)
    });
} else {
    hintBtn.on("touchstart", function () {
        hintImg.css("height", "100%");
    }).on("touchend", function () {
        hintImg.css("height", 0)
    });
}
