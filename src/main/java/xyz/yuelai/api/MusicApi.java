package xyz.yuelai.api;

import com.dtflys.forest.annotation.GetRequest;
import com.dtflys.forest.annotation.Query;

/**
 * @author zhong
 * @date 2021-03-17 15:02:10 周三
 */
public interface MusicApi {

    @GetRequest(url = "http://musicapi.leanapp.cn/search")
    String search(@Query("keywords") String keywords);

//    @GetRequest(url = "https://api.imjad.cn/cloudmusic/?type=song&search_type=1")
    @GetRequest(url = "http://localhost:3000/song/url")
    String detail(@Query("id") long id);
}
