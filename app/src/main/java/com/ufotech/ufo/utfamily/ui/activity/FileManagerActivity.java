package com.ufotech.ufo.utfamily.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.ui.base.BaseActivity;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 06:06
 *  @description ：   檔案總管
 *  -----------------------------------------------------------------------------------------
 */
public class FileManagerActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private ImageView iv_top_bar_start, iv_top_bar_end;
    private TextView tv_top_bar_title;
    private static final String ROOT = "/";
    private static final String PRE_LEVEL = "..";
    public static final int FIRST_ITEM = 0;
    public static final int SECOND_ITEM = 1;
    private String IMG_ITEM = "image";
    private String NAME_ITEM = "name";
    private List<Map<String, Object>> filesList;
    private List<String> names;
    private List<String> paths;
    private File[] files;
    private Map<String, Object> filesMap;
    private int[] fileImg = {
            R.drawable.directory,
            R.drawable.file};
    private SimpleAdapter simpleAdapter;
    private ListView listView;
    private String nowPath;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_file_manager;
    }

    @Override
    public void initView() {
        iv_top_bar_start = (ImageView) findViewById(R.id.iv_top_bar_start);
        iv_top_bar_end = (ImageView) findViewById(R.id.iv_top_bar_end);
        tv_top_bar_title = (TextView) findViewById(R.id.tv_top_bar_title);
        tv_top_bar_title.setText(getText(R.string.label_FileManager));
        filesList = new ArrayList<>();
        names = new ArrayList<>();
        paths = new ArrayList<>();
        getFileDirectory(ROOT);
        simpleAdapter = new SimpleAdapter(this,
                filesList, R.layout.adapter_file_manager, new String[]{IMG_ITEM, NAME_ITEM},
                new int[]{R.id.image_file_manager, R.id.text_file_manager});
        listView = (ListView) findViewById(R.id.list_file_manager);
        listView.setAdapter(simpleAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        iv_top_bar_start.setOnClickListener(this);
        iv_top_bar_end.setOnClickListener(this);
        // 瀏覽檔案
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String target = paths.get(position);
                if(target.equals(ROOT)){
                    nowPath = paths.get(position);
                    getFileDirectory(ROOT);
                    simpleAdapter.notifyDataSetChanged();
                } else if(target.equals(PRE_LEVEL)){
                    nowPath = paths.get(position);
                    getFileDirectory(new File(nowPath).getParent());
                    simpleAdapter.notifyDataSetChanged();
                } else {
                    File file = new File(target);
                    if (file.canRead()) {
                        if (file.isDirectory()) {
                            nowPath = paths.get(position);
                            getFileDirectory(paths.get(position));
                            simpleAdapter.notifyDataSetChanged();
                        } else{
                            Toast.makeText(FileManagerActivity.this, R.string.is_not_directory, Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(FileManagerActivity.this, R.string.can_not_read, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getFileDirectory(String path){
        filesList.clear();
        paths.clear();
        if(!path.equals(ROOT)){
            // 回根目錄
            filesMap = new HashMap<>();
            names.add(ROOT);
            paths.add(FIRST_ITEM, ROOT);
            filesMap.put(IMG_ITEM, fileImg[0]);
            filesMap.put(NAME_ITEM, ROOT);
            filesList.add(filesMap);
            //回上一層
            filesMap = new HashMap<>();
            names.add(PRE_LEVEL);
            paths.add(SECOND_ITEM, new File(path).getParent());
            filesMap.put(IMG_ITEM, fileImg[0]);
            filesMap.put(NAME_ITEM, PRE_LEVEL);
            filesList.add(filesMap);
        }

        files = new File(path).listFiles();
        for (File file : files) {
            filesMap = new HashMap<>();
            names.add(file.getName());
            paths.add(file.getPath());
            if (file.isDirectory()) {
                filesMap.put(IMG_ITEM, fileImg[0]);
            } else {
                filesMap.put(IMG_ITEM, fileImg[1]);
            }
            filesMap.put(NAME_ITEM, file.getName());
            filesList.add(filesMap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_bar_start:
                Log.d(TAG, "返回");
                finish();
                break;
            case R.id.iv_top_bar_end:
                break;
        }
    }
}