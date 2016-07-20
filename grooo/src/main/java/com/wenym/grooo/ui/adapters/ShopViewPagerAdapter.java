package com.wenym.grooo.ui.adapters;

import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ItemShoplistBinding;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.util.RxEvent.ImageEvent;
import com.wenym.grooo.util.RxJava.RxBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.Observable;

/**
 * Created by runzii on 16-7-3.
 */
public class ShopViewPagerAdapter extends PagerAdapter {


    int oldPosition = -1;
    private List<ItemShoplistBinding> bindings = new ArrayList<>();
    private List<List<Shop>> items = new ArrayList<>();

    public void setShops(List<Shop> shops) {
        items.clear();
        Observable.from(shops)
                .toMultimap(shop -> shop.getCategory(),
                        shop1 -> shop1,
                        () -> new HashMap<>()
                        , s -> new ArrayList<>())
                .subscribe(stringCollectionMap -> {
                    for (Map.Entry<String, Collection<Shop>> set : stringCollectionMap.entrySet()) {
                        items.add((List<Shop>) set.getValue());
                        notifyDataSetChanged();
                    }
                });
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ItemShoplistBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.item_shoplist, container, false);
        bindings.add(binding);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        binding.recyclerView.setAdapter(new ShopListAdapter(items.get(position)));
//        binding.setList(items.get(position));
        container.addView(bindings.get(position).recyclerView);
        return binding;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        //only if position changed
        if (position == oldPosition)
            return;
        oldPosition = position;

//        bindings.get(position).setList(items.get(position));

        Random random = new Random();
        int color = 0xff000000 | random.nextInt(0x00ffffff);
        String imageUrl = "";

        switch (random.nextInt(10)) {
            case 0:
                imageUrl = "http://www.cnwest88.com/uploadfile/2012/0509/20120509094628310.jpg";
                break;
            case 1:
                imageUrl = "http://www.izmzg.com/empirecms/d/file/zmzg/beijing/jingdian/2012-03-16/a60ca4bef8ab6c4343bc486a548610ea.jpg";
                break;
            case 2:
                imageUrl = "http://img.kpkpw.com/201106/27/14696_1309186930X2Mt.jpg";
                break;
            case 3:
                imageUrl = "http://img1.58.com/groupbuy/n_s12275644173422709133%2120811748818944.jpg";
                break;
            case 4:
                imageUrl = "http://www.nmghuana.com/UploadFiles/2014-03/nmghuana/2014032610591940146.jpg";
                break;
            case 5:
                imageUrl = "http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1107/30/c0/8490657_8490657_1311991396343.jpg";
                break;
            case 6:
                imageUrl = "http://www.ctps.cn/PhotoNet/Profiles/BLUE%E6%9D%B0/2009326130172166.jpg";
                break;
            case 7:
                imageUrl = "http://f.hiphotos.bdimg.com/album/w%3D2048/sign=f28844ce7e3e6709be0042ff0fff9e3d/962bd40735fae6cd2e8713880eb30f2442a70f23.jpg";
                break;
            case 8:
                imageUrl = "http://www.cdtianya.com/baike/uploads/201303/1363830542ySdkzSVt.jpg";
                break;
            case 9:
                imageUrl = "http://c.hiphotos.bdimg.com/album/w%3D2048/sign=e14035a13812b31bc76cca29b220347a/63d0f703918fa0ec9a37cdeb279759ee3c6ddb60.jpg";
                break;
        }

        RxBus.getDefault().post(new ImageEvent(imageUrl, color));

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(bindings.get(position).recyclerView);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).get(0).getCategory();
    }
}
