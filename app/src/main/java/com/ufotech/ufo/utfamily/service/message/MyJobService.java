package com.ufotech.ufo.utfamily.service.message;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * -----------------------------------------------------------------
 * @author ：UFO（陳俊融）Github：https://github.com/jyunrong
 * @date ：2018/5/15 上午 11:57
 * @description ：處理10秒以上的長時間任務
 * -----------------------------------------------------------------
 */
public class MyJobService extends JobService {

    private static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}