package xyz.yuelai.domain;

import java.util.List;

/**
 * @author zhong
 * @date 2021-03-17 15:15:42 周三
 */
public class SearchResult {
    private Integer code;
    private Boolean hasMore;
    private Integer songCount;
    private List<MusicInfo> songs;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Integer getSongCount() {
        return songCount;
    }

    public void setSongCount(Integer songCount) {
        this.songCount = songCount;
    }

    public List<MusicInfo> getSongs() {
        return songs;
    }

    public void setSongs(List<MusicInfo> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "code=" + code +
                ", hasMore=" + hasMore +
                ", songCount=" + songCount +
                ", songs=" + songs +
                '}';
    }
}
