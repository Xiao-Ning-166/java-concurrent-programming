package com.example.concurrent.template;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "log.test-tow-phase-termination")
public class TestTowPhaseTermination {

    public static void main(String[] args) throws InterruptedException {
        TowPhaseTermination towPhaseTermination = new TowPhaseTermination();

        towPhaseTermination.start();

        TimeUnit.SECONDS.sleep(5);

        towPhaseTermination.stop();;
    }

}

@Slf4j(topic = "log.tow-phase-termination")
class TowPhaseTermination {

    /**
     * 监控线程
     */
    private Thread monitor;

    /**
     * 启动监控线程
     */
    public void start() {
        this.monitor = new Thread(() -> {
            while (true) {
                // 判断是否被打断
                Thread currentThread = Thread.currentThread();
                boolean interrupted = currentThread.isInterrupted();
                if (interrupted) {
                    // 被打断了，料理后事
                    log.debug("被打断了，料理后事。。。。。。");
                    break;
                }
                try {
                    // 睡眠2秒
                    TimeUnit.SECONDS.sleep(2);
                    // 执行监控记录
                    log.debug("执行监控记录。。。。。。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 重新设置打断标记
                    currentThread.interrupt();
                }
            }
        });

        this.monitor.start();
    }

    /**
     * 停止监控线程
     */
    public void stop() {
        this.monitor.interrupt();
    }

}
