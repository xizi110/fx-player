package xyz.yuelai.domain;

import javafx.util.Duration;
import xyz.yuelai.util.FXUtil;

/**
 * @author zhong
 * @date 2021-03-17 15:27:03 周三
 */
public class MusicInfo {
    /**
     * 歌曲id
     */
    private Long id;
    /**
     * 歌曲名
     */
    private String name;
    /**
     * 专辑名
     */
    private String albumName;
    /**
     * 歌手名
     */
    private String artistName;
    /**
     * 时间
     */
    private Integer duration;
    /**
     * fee，1是需要vip
     */
    private Integer fee;

    private String formatTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getFormatTime() {
        return FXUtil.formatMusicTime(Duration.millis(duration));
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", albumName='" + albumName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", formatTime=" + formatTime +
                ", fee=" + fee +
                '}';
    }
}
