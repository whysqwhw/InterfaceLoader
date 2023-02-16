package com.jeremyliao.android.service.loader.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.jeremyliao.android.service.loader.InterfaceLoader;
import com.jeremyliao.android.service.loader.app.databinding.ActivityMainBinding;
import com.jeremyliao.android.service.loader.app.service.LoaderDemoService;
import com.jeremyliao.android.service.loader.fetcher.ServiceFetcher;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ServiceFetcher serviceFetcher;
    final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(this);
        binding.setLifecycleOwner(this);

        Intent intent = new Intent(LoaderDemoService.ACTION);
        intent.setPackage(getPackageName());
        serviceFetcher = new ServiceFetcher(this, intent);
        serviceFetcher.bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serviceFetcher.unbindService();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void remotePlus() {
        disposable.add(serviceFetcher.getService()
                .map(binder -> InterfaceLoader.getService(ILoaderDemo.class, binder).plus(10, 20))
                .map(result -> "result: " + result)
                .subscribe(result -> binding.tvContent.setText(result)));
    }

    public void remoteMinus() {
        disposable.add(serviceFetcher.getService()
                .map(binder -> InterfaceLoader.getService(ILoaderDemo.class, binder).minus(10, 20))
                .map(result -> "result: " + result)
                .subscribe(result -> binding.tvContent.setText(result)));
    }

    public void remoteMulti() {
        disposable.add(serviceFetcher.getService()
                .map(binder -> InterfaceLoader.getService(ILoaderDemo.class, binder).multi(10, 20))
                .map(result -> "result: " + result)
                .subscribe(result -> binding.tvContent.setText(result)));
    }
}