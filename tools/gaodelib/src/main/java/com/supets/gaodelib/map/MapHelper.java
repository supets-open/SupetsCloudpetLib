package com.supets.gaodelib.map;

import android.content.Context;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.poisearch.PoiSearch;

/**
 * SupetsCloudpetLib
 *
 * @user lihongjiang
 * @description
 * @date 2017/9/27
 * @updatetime 2017/9/27
 */

public class MapHelper {

    public static final String mType = "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";

    //地址转坐标
    public static void geoSearch(Context context, String name, String cityCode, GeocodeSearch.OnGeocodeSearchListener listener) {
        GeocodeSearch mGeocoderSearch = new GeocodeSearch(context);
        mGeocoderSearch.setOnGeocodeSearchListener(listener);
        GeocodeQuery query = new GeocodeQuery(name, cityCode);
        mGeocoderSearch.getFromLocationNameAsyn(query);
    }

    //坐标转地址
    public static void geoSearch(Context context, LatLonPoint mCurrentPoint, GeocodeSearch.OnGeocodeSearchListener listener) {
        GeocodeSearch mGeocoderSearch = new GeocodeSearch(context);
        mGeocoderSearch.setOnGeocodeSearchListener(listener);
        RegeocodeQuery query = new RegeocodeQuery(mCurrentPoint, 200, GeocodeSearch.AMAP);
        mGeocoderSearch.getFromLocationAsyn(query);
    }

    public static void searchArround(Context context,
                                     int page,
                                     LatLonPoint mCurrentPoint,
                                     PoiSearch.OnPoiSearchListener listener) {
        PoiSearch.Query mPoiQuery = new PoiSearch.Query("", mType, "");
        mPoiQuery.setPageSize(50);// 设置每页最多返回多少条poiitem
        mPoiQuery.setPageNum(page);//设置查第一页

        PoiSearch poiSearch = new PoiSearch(context, mPoiQuery);
        poiSearch.setOnPoiSearchListener(listener);
        //设置周边搜索的中心点以及区域
        poiSearch.setBound(new PoiSearch.SearchBound(mCurrentPoint, 1500, true));
        poiSearch.searchPOIAsyn();//开始搜索
    }

    public static void searchKeyWord(Context context,
                                     int page, String keyword,
                                     PoiSearch.OnPoiSearchListener listener) {
        PoiSearch.Query mPoiQuery = new PoiSearch.Query(keyword, mType, "");
        mPoiQuery.setPageSize(50);// 设置每页最多返回多少条poiitem
        mPoiQuery.setPageNum(page);//设置查第一页
        PoiSearch poiSearch = new PoiSearch(context, mPoiQuery);
        poiSearch.setOnPoiSearchListener(listener);
        poiSearch.searchPOIAsyn();//开始搜索
    }

    public static void moveCenterDian(MapView  mapView, LatLng dian){
            mapView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(dian, 15));
    }
}
