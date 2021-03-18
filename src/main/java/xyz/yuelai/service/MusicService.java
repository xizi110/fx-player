package xyz.yuelai.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import xyz.yuelai.api.MusicApi;
import xyz.yuelai.config.ForestConfig;
import xyz.yuelai.domain.MusicInfo;
import xyz.yuelai.domain.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhong
 * @date 2021-03-17 15:34:19 周三
 */
public class MusicService {

    private MusicApi musicApi = ForestConfig.createInstance(MusicApi.class);

    public SearchResult search(String keywords) {
        String search = musicApi.search(keywords);
        JSONObject jsonObject = JSONObject.parseObject(search);
        Integer code = jsonObject.getInteger("code");
        if (!Objects.equals(code, 200)) {
            return null;
        }
        SearchResult searchResult = new SearchResult();
        JSONObject result = jsonObject.getJSONObject("result");
        Boolean hasMore = result.getBoolean("hasMore");
        Integer songCount = result.getInteger("songCount");
        searchResult.setCode(code);
        searchResult.setHasMore(hasMore);
        searchResult.setSongCount(songCount);
        List<MusicInfo> musicInfos = new ArrayList<>();
        JSONArray songs = result.getJSONArray("songs");
        for (int i = 0; i < songs.size(); i++) {
            JSONObject song = songs.getJSONObject(i);
            Long id = song.getLong("id");
            String name = song.getString("name");
            Integer duration = song.getInteger("duration");
            Integer fee = song.getInteger("fee");
            String albumName = song.getJSONObject("album").getString("name");
            String artistName = song.getJSONArray("artists").getJSONObject(0).getString("name");
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setId(id);
            musicInfo.setName(name);
            musicInfo.setDuration(duration);
            musicInfo.setFee(fee);
            musicInfo.setAlbumName(StringUtils.isEmpty(albumName) ? "" : "《" + albumName + "》");
            musicInfo.setArtistName(artistName);
            musicInfos.add(musicInfo);
        }
        searchResult.setSongs(musicInfos);
        return searchResult;
    }

    public String getUrl(long id) {
        String detailResult = musicApi.detail(id);
        JSONObject jsonObject = JSONObject.parseObject(detailResult);
        JSONArray data = jsonObject.getJSONArray("data");
        System.out.println(data);
        if (data.size() > 0) {
            return data.getJSONObject(0).getString("url");
        }
        return null;
    }

}
