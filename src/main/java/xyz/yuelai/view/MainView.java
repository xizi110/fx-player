package xyz.yuelai.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yuelai.Receiver;
import xyz.yuelai.View;
import xyz.yuelai.control.SVG;
import xyz.yuelai.service.MusicService;
import xyz.yuelai.util.FXUtil;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author zhong
 * @date 2021-03-15 17:25:05 周一
 */
public class MainView extends View {

    private static final Logger logger = LoggerFactory.getLogger(SearchView.class);

    @Override
    public String fxml() {
        return "/view/Main.fxml";
    }

    @FXML
    private MediaView mediaView;
    @FXML
    private Button control;
    @FXML
    private Label progressTime;
    @FXML
    private Label totalTime;
    @FXML
    private Slider slider;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Button volume;
    @FXML
    private StackPane content;

    private MediaPlayer mediaPlayer;
    private BooleanProperty playing;
    private SearchView searchView;
    private MusicService musicService;
    private double muteBeforeVolume;
    private Duration seekTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化对象
        searchView = new SearchView();
        musicService = new MusicService();
        playing = new SimpleBooleanProperty(false);

        // 初始化页面
        content.getChildren().add(searchView.getRoot());

        statusChangeListener = (observable, oldValue, newValue) -> statusChange(newValue);
        playStatusChangeListener = (observable, oldValue, newValue) -> {
            if (newValue) {
                mediaPlayer.play();
                control.setGraphic(readSvgFxml("/assets/icon/pause.fxml"));
                logger.info("开始播放...");
            } else {
                mediaPlayer.pause();
                control.setGraphic(readSvgFxml("/assets/icon/play.fxml"));
                logger.info("播放暂停！");
            }
        };

        muteChangeListener = (observable, oldVal, newVal) -> {
            if (newVal) {
                // 记录静音时的音量值
                muteBeforeVolume = mediaPlayer.getVolume();
                volumeSlider.setValue(0);
                logger.debug("静音！");
            } else {
                // 取消静音，恢复静音前的音量值
                volumeSlider.setValue(muteBeforeVolume);
                logger.debug("取消静音！");
            }
        };

        volumeChangeListener = (observable, oldValue, newValue) -> {
            if (newValue.doubleValue() > 0) {
                mediaPlayer.setMute(false);
                volume.setGraphic(readSvgFxml("/assets/icon/volume.fxml"));
            } else {
                volume.setGraphic(readSvgFxml("/assets/icon/volume-mute.fxml"));
            }
        };

        currentTimeChangeListener = (observable, oldValue, newValue) -> {
            // 播放期间拖动进度条，不更新显示的时间
            if (!slider.isPressed()) {
                // 点击进度条，跳转指定时间，需要判断点击后的当前时间是否大于跳转的时间，
                // 并且点击后的当前时间与跳转时间间隔不小于100ms，因为跳转时间不是实时的，
                // newVal 可能是之前的旧值，所以需要判断新的当前时间是在跳转时间附近
                if (seekTime != null && newValue.greaterThanOrEqualTo(seekTime) && newValue.subtract(seekTime).toMillis() < 1000){
                    seekTime = null;
                }else if (seekTime == null){
                    // 如果没有点击进度条,或者已缓冲完成则正常更新时间
                    progressTime.setText(FXUtil.formatMusicTime(newValue));
                    slider.setValue(newValue.toMillis());
                }
                // 如果缓冲没有完成，则什么也不做
            }
        };
    }

    private ChangeListener<MediaPlayer.Status> statusChangeListener;
    private ChangeListener<Boolean> playStatusChangeListener;
    private ChangeListener<Boolean> muteChangeListener;
    private ChangeListener<Number> volumeChangeListener;
    private ChangeListener<Duration> currentTimeChangeListener;


    private void statusChange(MediaPlayer.Status newStatus) {
        switch (newStatus) {
            case READY: {
                playerReady();
                break;
            }
            // 停止、暂停和出错都是未播放状态
            case STOPPED:
            case PAUSED:
            case HALTED: {
                playing.set(false);
                break;
            }
            // 缓冲，播放都是播放状态
            case STALLED:
            case PLAYING: {
                playing.set(true);
                break;
            }
            default: {
                playing.set(false);
                disposed();
            }
        }
    }

    /**
     * 释放资源
     */
    private void disposed() {
        mediaPlayer.statusProperty().removeListener(statusChangeListener);
        playing.removeListener(playStatusChangeListener);
        mediaPlayer.muteProperty().removeListener(muteChangeListener);
        mediaPlayer.volumeProperty().removeListener(volumeChangeListener);
        mediaPlayer.currentTimeProperty().removeListener(currentTimeChangeListener);
        mediaPlayer.volumeProperty().unbind();
    }

    /**
     * 播放器准备就绪，做一些初始化工作
     */
    private void playerReady() {
        // 开始时间
        Duration startTime = mediaPlayer.getStartTime();
        // 音乐总时间
        Duration totalDuration = mediaPlayer.getTotalDuration();

        progressTime.setText(FXUtil.formatMusicTime(startTime));
        totalTime.setText(FXUtil.formatMusicTime(totalDuration));

        // 音乐进度条最小值，起始时间
        slider.setMin(startTime.toMillis());
        // 音乐进度条最大值，总时间
        slider.setMax(totalDuration.toMillis());
        // 当前进度为0
        slider.setValue(0);
        progressTime.setText("00:00");

        // 音量调节绑定滑动条
        mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());

        // 播放与暂停状态图标切换
        playing.addListener(playStatusChangeListener);

        // 监听静音与否
        mediaPlayer.muteProperty().addListener(muteChangeListener);

        // 监听音量变化
        mediaPlayer.volumeProperty().addListener(volumeChangeListener);

        // 监听当前时间，更新进度条和时间
        mediaPlayer.currentTimeProperty().addListener(currentTimeChangeListener);

        seekTime = null;
        // 点击进度条，鼠标释放，切换播放时间
        slider.setOnMouseReleased(event -> {
            seekTime = Duration.millis(slider.getValue());
            mediaPlayer.seek(Duration.millis(slider.getValue()));
            // 不会更新当前时间，需要手动设置
            progressTime.setText(FXUtil.formatMusicTime(Duration.millis(slider.getValue())));
        });

        // 播放完毕结束
        mediaPlayer.setOnEndOfMedia(() -> {
            slider.setValue(0);
            progressTime.setText("00:00");
            mediaPlayer.stop();
        });
        logger.info("播放器准备完毕!");
    }

    @FXML
    private void playOrPause() {
        if (mediaPlayer != null && mediaPlayer.getMedia() != null) {
            playing.set(playing.not().get());
        }
    }

    @FXML
    private void prev(ActionEvent event) {

    }

    @FXML
    private void next(ActionEvent event) {

    }

    /**
     * 切换静音
     *
     * @param event
     */
    @FXML
    private void volumeOnOff(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.setMute(!mediaPlayer.isMute());
        }
    }

    /**
     * 播放音乐的方法
     *
     * @param source 待播放的资源uri
     */
    @Receiver(name = "mainView:play")
    public void play(String source) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        Media media = new Media(source);
        createPlayer(media);
    }

    /**
     * 创建播放器并播放
     *
     * @param media 播放的媒体
     */
    private void createPlayer(Media media) {
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.statusProperty().addListener(statusChangeListener);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
    }

    /**
     * 读取 SVG fxml
     *
     * @param path SVG fxml文件位置
     * @return
     */
    private SVG readSvgFxml(String path) {
        try {
            return FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
