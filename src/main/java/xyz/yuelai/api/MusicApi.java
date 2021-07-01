package xyz.yuelai.api;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.GetRequest;
import com.dtflys.forest.annotation.Query;

/**
 * @author zhong
 * @date 2021-03-17 15:02:10 周三
 * 需要部署网易云接口，此处使用 网易云音乐 Node.js API service
 * GitHub 地址 https://github.com/xizi110/NeteaseCloudMusicApi
 */
@BaseRequest(baseURL = "https://netease-cloud-music-api-ten-orcin.vercel.app")
public interface MusicApi {

    @GetRequest(url = "/search")
    String search(@Query("keywords") String keywords, int offset, int limit);

    @GetRequest(url = "/song/url")
    String detail(@Query("id") long id);
}
