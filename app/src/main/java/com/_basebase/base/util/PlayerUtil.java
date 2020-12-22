package com._basebase.base.util;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerUtil {
    private static ExecutorService executorService;
    private static File audioFile;
    private static Handler mainThreadHandler;
    // volatile 能及时同步到主内存(不在本线程读取的)中,避免脏读
    private static volatile boolean isPlaying;
    private static volatile boolean isPrepared;
    private static MediaPlayer mediaPlayer;
    private static Runnable playerFailRemind;
    private static Runnable playerCompletionListener;
    private static Runnable playerErrorListener;
    private static Runnable playerPreparedListener;
    private static PlayProgressListener playProgressListener;
    private static int currentProgress;
    private static int totalProgress;

    public interface PlayProgressListener {
        void playProgress(float percentage);
    }

    public static int getTotalProgress() {
        return totalProgress;
    }

    public static boolean isIsPlaying() {
        return isPlaying;
    }

    public static boolean isIsPrepared() {
        return isPrepared;
    }

    public static void setPlayerCompletionListener(Runnable playerCompletionListener) {
        PlayerUtil.playerCompletionListener = playerCompletionListener;
    }

    public static void setPlayerErrorListener(Runnable playerErrorListener) {
        PlayerUtil.playerErrorListener = playerErrorListener;
    }

    public static void setPlayerPreparedListener(Runnable playerPreparedListener) {
        PlayerUtil.playerPreparedListener = playerPreparedListener;
    }

    public static int getCurrentProgress() {
        return currentProgress;
    }

    private static void init() {
        // 录音JNI函数不具备线程安全性，所以要用单线程
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        if (mainThreadHandler == null) {
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }
    }

    private static void execPlayProgress() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer == null) {
                    return;
                }
                currentProgress = mediaPlayer.getCurrentPosition();
                mainThreadHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (playProgressListener != null) {
                            playProgressListener.playProgress((currentProgress + 1000) / totalProgress);
                        }
                        if (isPlaying) {
                            execPlayProgress();
                        }
                    }
                }, 1000);
            }
        });
    }

    public static void setPlayerFailRemind(Runnable fail) {
        playerFailRemind = fail;
    }

    public static void setPlayProgressListener(PlayProgressListener listener) {
        playProgressListener = listener;
    }

    public static void continuePlay(File file) {
        init();
        audioFile = file;
        // 检查当前状态，防止重复播放
        if (audioFile != null && !isPlaying) {
            // 提交后台任务，开始播放
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    doPlay(audioFile);
                }
            });
        }
    }

    public static void continuePlay() {
        init();
        // 设置当前播放状态
        isPlaying = true;
        // 检查当前状态，防止重复播放
        if (audioFile != null && isPrepared) {
            // 提交后台任务，开始播放
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.start();
                    execPlayProgress();
                }
            });
        }
    }

    public static void seekTo(final float percentage) {   //百分比进度
        if (isPrepared) {   //必须缓冲完成后进行
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.seekTo((int) (mediaPlayer.getDuration() * percentage));
                }
            });
        }
    }

    public static void seekTo(final int progress) {   //百分比进度
        if (isPrepared) {   //必须缓冲完成后进行
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.seekTo(progress);
                }
            });
        }
        PlayerUtil.currentProgress = progress;
    }

    public static void pause() {
        if (executorService != null) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    doPause();
                }
            });
        }
    }

    public static void stop() {
        if (executorService != null) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    stopPlay();
                }
            });
        }
    }

    public static void destroy() {
        if (mainThreadHandler != null) {
            mainThreadHandler.removeCallbacksAndMessages(null);
        }
        stop();
        audioFile = null;
        mainThreadHandler = null;
        isPlaying = false;
        isPrepared = false;
        mediaPlayer = null;
        playerFailRemind = null;
        playProgressListener = null;
        playerCompletionListener = null;
        playerPreparedListener = null;
        playerErrorListener = null;

    }

    /**
     * 播放录音文件
     *
     * @param audioFile
     */
    private static void doPlay(File audioFile) {
        stopPlay(); //是否有播放,有就释放
        isPlaying = true;   //重置标志,被stop重置了
        // 配置播放器 MediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            // 设置声音文件 或者网络地址  https会报错
            mediaPlayer.setDataSource(audioFile.getAbsolutePath());
            // 设置监听回调
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 播放结束， 释放播放器
                    stopPlay();
                    if (playerCompletionListener != null) {
                        playerCompletionListener.run();
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 提示用户
                    playFail();
                    // 释放播放器
                    stopPlay();
                    // 错误已经处理，返回true
                    if (playerErrorListener != null) {
                        playerErrorListener.run();
                    }
                    return true;
                }
            });
            // 配置音量， 是否循环
//            mediaPlayer.setVolume(1, 1);
            mediaPlayer.setLooping(false);
            // 准备，开始
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPrepared = true;
                    if (playerPreparedListener != null) {
                        playerPreparedListener.run();
                    }
                    mediaPlayer.start();
                    if (isPlaying) {
                        execPlayProgress();
                    }
                    if (currentProgress >= 0 && currentProgress <= mediaPlayer.getDuration()) {   //处理跳转到某个进度
                        mediaPlayer.seekTo(currentProgress);
                    }
                    totalProgress = mediaPlayer.getDuration();
                }
            });
            mediaPlayer.prepareAsync(); //最好缓冲异步,不异步容易报错 --- 手机处理慢或者文件大时容易报错
        } catch (IOException | RuntimeException e) {
            // 异常处理，防止闪退
//           todo  e.printStackTrace();
            // 提示用户
            playFail();
            // 释放播放器
            stopPlay();
        }

    }

    // 暂停
    private static void doPause() {
        if (isPrepared) {    //已经缓冲完成播放了
            isPlaying = false;
            mediaPlayer.pause();
        } else if (isPlaying) { //设置播放但是没有缓冲完成播放
            isPlaying = false;  //直接把播放标志设置成false
        }

    }

    /**
     * 停止播放逻辑
     */
    private static void stopPlay() {
        // 重置播放状态
        isPlaying = false;
        isPrepared = false;
        // 释放播放器
        if (mediaPlayer != null) {
            // 重置播放器， 防止内存泄漏
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 提醒用户播放失败
     */
    private static void playFail() {
        if (playerFailRemind != null && mainThreadHandler != null) {
            // 在主线程toast提示
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    playerFailRemind.run();
                }
            });
        }
    }

}

