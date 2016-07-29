package com.wenym.grooo.provider;

import java.util.Random;

/**
 * Created by runzii on 16-7-21.
 */
public class ImageBacks {

    private static final String[] images =
            {
                    "http://www.izmzg.com/empirecms/d/file/zmzg/beijing/jingdian/2012-03-16/a60ca4bef8ab6c4343bc486a548610ea.jpg",
                    "http://img.kpkpw.com/201106/27/14696_1309186930X2Mt.jpg",
                    "http://img1.58.com/groupbuy/n_s12275644173422709133%2120811748818944.jpg",
                    "http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1107/30/c0/8490657_8490657_1311991396343.jpg",
                    "http://www.ctps.cn/PhotoNet/Profiles/BLUE%E6%9D%B0/2009326130172166.jpg",
                    "http://f.hiphotos.bdimg.com/album/w%3D2048/sign=f28844ce7e3e6709be0042ff0fff9e3d/962bd40735fae6cd2e8713880eb30f2442a70f23.jpg",
                    "http://www.cdtianya.com/baike/uploads/201303/1363830542ySdkzSVt.jpg",
                    "http://c.hiphotos.bdimg.com/album/w%3D2048/sign=e14035a13812b31bc76cca29b220347a/63d0f703918fa0ec9a37cdeb279759ee3c6ddb60.jpg",
            };

    public static String getOne() {
        return images[new Random().nextInt(images.length)];
    }
}
