let puzzle = {
    row: 0,// 行数
    col: 0,// 列数
    steps: 0,// 当前步数
    bSize: 1,// 边框大小
    bWidth: 0,// 盒子宽度
    bHeight: 0,// 盒子高度
    gWidth: 0,// 格子宽度
    gHeight: 0,// 格子高度
    bTop: 0,// 空白格Top值
    totolSteps: 0,// 总步数
    startTime: 0,// 开始时间
    bLeft: 0,// 空白格Left值
    bIndex: 0,// 空白格索引值
    grids: [],// 存储格子的数组
    autoSpeed: 150,// 自动移动速度
    moveSpeed: 120, // 方块移动速度
    currStatus: "",// 初始状态字符串
    finalStatus: "",// 最终状态字符串
    isMoving: false,// 格子是否正在移动
    reSet: function () {
        this.moveSpeed = 120;
        this.autoSpeed = 150;
        $(".hint-img, .grid").css("background-image", imgSrc);
        this.updateBorder(1);
    },
    init: function (row, col) {// 初始化设置
        if (puzzle.isMoving) return;// 格子正在移动，不作出响应
        box.empty();// 清空格子
        this.row = row;// 设置行
        this.col = col;// 设置列
        let bSize = this.bSize;// 获取边框大小
        this.bWidth = box.width();// 设置盒子的宽
        this.bHeight = box.height();// 设置盒子的高
        hintImg.css("height", "100%");// 显示全图
        util.disableOperate(autoBtn, hintBtn);// 禁用自动按钮、提示按钮
        this.finalStatus = util.getFinalStatus(row, col);// 获取最终状态字符串
        let gWidth = this.gWidth = (this.bWidth - (col + 1) * bSize) / col;// 计算格子的宽
        let gHeight = this.gHeight = (this.bHeight - (row + 1) * bSize) / row;// 计算格子的高
        for (let i = 0; i < row * col; i++) {
            let r = Math.floor(i / row), c = i % row;// 当前索引对应的行、列
            let gTop = gHeight * r + (r + 1) * bSize;// 计算当前索引对应的位置
            let gLeft = gWidth * c + (c + 1) * bSize;
            if (i < row * col - 1) {
                let grid = $("<div class='grid'></div>"); // 创建格子结点
                grid.css("top", gTop).css("left", gLeft)
                    .css("width", gWidth).css("height", gHeight)
                    .css("background-position-x", 0 - c * gWidth)
                    .css("background-position-y", 0 - r * gHeight)
                    .css("background-size", row * 100 + "% " + col * 100 + "%")
                    .css("background-image", hintImg.css("backgroundImage"));// 初始化当前格的样式
                box.append(grid);// 将创建好的格子放入盒子里面
            } else {
                this.bIndex = i;// 记录空白格的索引
                this.bTop = gTop;// 记录空白格的位置
                this.bLeft = gLeft;
            }
        }
        gridsNode = $(".grid");// 存储所有格子结点
    },
    updateBorder: function (bSize) {
        if (puzzle.isMoving) return;// 格子正在移动，不作出响应
        this.bSize = bSize;// 更新边框大小
        let row = this.row, col = this.col;
        let gWidth = this.gWidth = (this.bWidth - (col + 1) * bSize) / col;// 计算格子的宽
        let gHeight = this.gHeight = (this.bHeight - (row + 1) * bSize) / row;// 计算格子的高
        this.isMoving = true;// 设置为正在移动
        for (let i = 0; i < row * col - 1; i++) {
            let grid = gridsNode.eq(i);
            let gIndex = grid.attr("index");// 获取格子索引序号
            let bgRow = Math.floor(i / row), bgCol = i % row;// 计算背景图片行、列
            let gRow = Math.floor(gIndex / row), gCol = gIndex % row;// 计算格子行、列
            let bgX = -bgCol * gWidth, bgY = -bgRow * gHeight;// 计算背景图片位置
            let gTop = gHeight * gRow + (gRow + 1) * bSize, gLeft = gWidth * gCol + (gCol + 1) * bSize;// 计算格子位置
            grid.css("top", gTop).css("left", gLeft)
                .css("background-position-x", bgX)
                .css("background-position-y", bgY)
                .css("width", gWidth).css("height", gHeight);// 更新当前格子样式
        }
        let bIndex = this.currStatus.indexOf('\u0000');// 空白格索引位置
        let bRow = Math.floor(bIndex / row), bCol = bIndex % row;// 计算空白格行、列
        this.bTop = gHeight * bRow + (bRow + 1) * bSize;// 更新空白格位置
        this.bLeft = gWidth * bCol + (bCol + 1) * bSize;
        this.isMoving = false;
    },
    layout: function () {// 布局
        let bSize = this.bSize;
        let row = this.row, col = this.col;
        let gWidth = this.gWidth, gHeight = this.gHeight;// 获取格子的宽、高
        this.isMoving = true;// 设置为正在移动
        for (let i = 0; i < row * col - 1; i++) {
            let ch = String.fromCharCode(97 + i);// 当前格对应的字母
            let gIndex = this.currStatus.indexOf(ch);// 字母所在的位置索引
            let gRow = Math.floor(gIndex / row), gCol = gIndex % row;// 行、列
            let gTop = gHeight * gRow + (gRow + 1) * bSize;// 位置
            let gLeft = gWidth * gCol + (gCol + 1) * bSize;
            this.grids[gIndex] = gridsNode.eq(i).attr("index", gIndex)
                .animate({top: gTop, left: gLeft}, 500, function () {
                    if (i === row * col - 2) puzzle.isMoving = false;
                });// 将对应格子移动到当前位置并存储
        }
    },
    run: function () {// 开始游戏
        if (puzzle.isMoving) return;// 格子正在移动，不作出响应
        let diff = parseInt($(".diff:checked").val());// 获取难度值
        if (this.row !== diff) this.init(diff, diff);// 行列值改变重新初始化
        util.advice("");// 清空通知
        hintImg.css("height", 0);// 隐藏全图
        stepsBox.text((puzzle.steps = 0));// 步数归0
        $(".minutes-box").empty().append(mCount);// 重新添加计时器
        $(".seconds-box").empty().append(sCount);
        mCount.css("animation-play-state", "running");// 开始计时动画
        sCount.css("animation-play-state", "running");
        this.startTime = new Date().getTime();// 记录开始时间
        this.currStatus = util.createStatus(this.finalStatus, this.row, this.col);// 根据最终状态生成一个可解的初始状态
        util.enableOperate(autoBtn, hintBtn);// 启用自动按钮、提示按钮、边框调节
        gridsNode.on("mousedown", this.conditionalMove);// 绑定点击事件
        this.layout();// 开始布局
    },
    move: function (grid, isAutoMove) {// 将参数指定的格子移动到当前空白格的位置
        puzzle.isMoving = true;// 设置为正在移动
        let gIndex = grid.attr("index");// 获取所点击格的索引
        let desCh = puzzle.currStatus.charAt(gIndex);// 获取所点击格当前对应的字符
        let bLeft = puzzle.bLeft, bTop = puzzle.bTop;// 获取游戏的空表格的位置，避免重复使用this调用（浅拷贝）
        let gTop = grid.position().top, gLeft = grid.position().left;// 获取当前格的位置数据
        puzzle.currStatus = puzzle.currStatus.replace('\u0000', '*')
            .replace(desCh, '\u0000')
            .replace('*', desCh);// 更新当前状态字符串
        grid.animate({top: bTop, left: bLeft}, puzzle.moveSpeed, function () {
            puzzle.grids[puzzle.bIndex] = grid;// 更新数组
            grid.attr("index", puzzle.bIndex);// 更新该格的索引属性值
            puzzle.bIndex = gIndex;// 将空白格索引更新成所点击格的索引
            [puzzle.bTop, puzzle.bLeft] = [gTop, gLeft];// 将空白格位置更新成所点击的位置
            if (puzzle.currStatus === puzzle.finalStatus) {
                mCount.css("animation-play-state", "paused");// 停止计时
                sCount.css("animation-play-state", "paused");
                gridsNode.unbind("mousedown");// 解除绑定点击事件
                util.disableOperate(autoBtn, hintBtn);// 禁用自动按钮、提示按钮
                let stopTime = Math.floor((new Date().getTime() - puzzle.startTime) / 1000);// 获取结束时的时间戳（秒）
                let seconds = stopTime % 60;// 计算秒数
                let minutes = Math.floor(stopTime / 60);// 计算分钟数
                let scanLine = $("<div id='scanLine'></div>");// 创建扫描线
                $("#puzzle").prepend(scanLine);// 添加扫描线
                scanLine.animate({top: puzzle.bHeight - scanLine.height() + "px"}, 1500, function () {
                    this.remove();// 扫描动画完成后移除扫描线
                });
                hintImg.animate({height: puzzle.bHeight + "px"}, 1500, function () {
                    processBar.css("width", "100%");// 复位进度条
                    util.advice("拼图完成，耗时" + (minutes ? minutes + "分" : "") + seconds + "秒");
                    puzzle.isMoving = puzzle.started = false;// 移动完成，设置为未正在移动，未开始
                    if (isAutoMove) util.enableOperate($(".btn, .diff, .border-size, .reset").not("#auto-btn,#hint-btn"));// 启用除自动按钮外的其他操作按钮
                });
            } else {
                puzzle.isMoving = puzzle.started = false;// 移动完成，设置为未正在移动，未开始
            }
            util.update(isAutoMove);
        });
    },
    conditionalMove: function () {
        if (puzzle.isMoving) return;// 格子正在移动，不作出响应
        let grid = $(this);// 获取当前格元素，避免重复使用$(this)调用
        let bIndex = puzzle.bIndex;// 获取空表格的索引
        let gIndex = grid.attr("index");// 获取所点击格的索引
        let col = puzzle.col;// 获取游戏的列值，避免重复使用this调用（浅拷贝）
        let moveAble = Math.abs(bIndex - gIndex) === col;// 通过位置计算出点击的格子是否可以上下移动
        moveAble |= bIndex - gIndex === 1 && bIndex % col !== 0;// 是否可以向右移动
        moveAble |= gIndex - bIndex === 1 && gIndex % col !== 0;// 是否可以向左移动
        if (moveAble) puzzle.move(grid, false);// 可移动，调用移动函数
    },
    auto: function () {
        if (puzzle.isMoving) return;// 格子正在移动，不作出响应
        let comps = $(".btn, .diff, .border-size, .reset");// 获取操作控件
        util.disableOperate(comps);// 禁用所有操作按钮
        gridsNode.unbind("mousedown");// 解除绑定点击事件
        util.advice("正在搜索......");
        util.showProcess();// 显示进度条
        $.ajax({
            url: "auto_complete",
            type: "POST",
            timeout: 30000,
            contentType: "application/json",
            data: JSON.stringify({
                row: this.row,
                col: this.col,
                currStatus: this.currStatus,
                finalStatus: this.finalStatus
            }),
            success: function (steps) {
                processBar.stop(true, true);// 停止进度条，直接到最终状态
                util.advice("完成中......");
                if (steps.length !== 0) {
                    stepsBox.text((puzzle.steps = puzzle.totolSteps = steps.length));
                    let doMove = null, i = 0;
                    setTimeout(doMove = function () {
                        if (i === steps.length) return;// 递归出口
                        // 格子移动未完成，取消本次移动操作
                        if (!puzzle.isMoving) {
                            puzzle.move(puzzle.grids[steps[i++] - 1], true);
                        }
                        setTimeout(doMove, puzzle.autoSpeed);// 递归调用
                    }, 0);
                } else {
                    util.enableOperate(comps);// 启用操作按钮
                    gridsNode.on("mousedown", puzzle.conditionalMove);// 绑定格子点击事件
                    util.advice("搜索超时，移动几步后再试");
                }
            },
            error: function () {
                util.enableOperate(comps);// 启用操作按钮
                gridsNode.on("mousedown", puzzle.conditionalMove);// 绑定格子点击事件
                processBar.stop(true, true);// 停止进度条，直接到最终状态
                util.advice("网络异常！");
            }
        });
    }
};
puzzle.init(3, 3);