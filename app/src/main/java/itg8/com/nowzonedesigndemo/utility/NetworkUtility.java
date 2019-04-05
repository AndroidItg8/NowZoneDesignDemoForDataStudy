package itg8.com.nowzonedesigndemo.utility;


import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.common.Retro;
import itg8.com.nowzonedesigndemo.common.RetroController;
import itg8.com.nowzonedesigndemo.utility.model.breath.BreathingModel;
import itg8.com.nowzonedesigndemo.utility.model.step.StepsModel;
import okhttp3.ResponseBody;

public  class NetworkUtility {


    public void login(String url,String username, String password, ResponseListener listener) {
        if(listener==null)
            throw new NullPointerException("null provided in NetworkUtility.login");
        Observable<ResponseBody> b=controller.checkLogin(url,username,password);
        b.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String response=responseBody.string();
                            listener.onSuccess(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onSomethingWrong(e);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailure(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void savePresureData(List<DataModelPressure> dataModelPressures, ResponseListener listener) {
        if(listener==null)
            return;
        Observable<ResponseBody> b=controller.storePressure(dataModelPressures);
        b.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String response=responseBody.string();
                            JSONObject jsonObject = null;
                            if(response!=null){
                                try {
                                    jsonObject= new JSONObject(response);
                                    if(jsonObject.has("flag")){
                                        if(jsonObject.getInt("flag")==1){
                                            if(jsonObject.has("msg"))
                                            listener.onSuccess(jsonObject.getString("msg"));
                                        }




                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    listener.onFailure(e.getMessage());
                                }

                               }

                        } catch (IOException e) {
                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailure(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public interface ResponseListener{
        void onSuccess(Object message);
        void onFailure(Object err);
        void onSomethingWrong(Object e);
    }

    private static RetroController controller;




    public NetworkUtility(NetworkBuilder builder) {
        controller= Retro.getInstance().getController(builder.token);
    }


    public void saveBreath(String url,BreathingModel model, ResponseListener listener){
       Observable<ResponseBody> b=controller.storeBreathing(url,model);
        b
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String res=responseBody.string();
                            if(res==null){
                                listener.onSomethingWrong(null);
                            }else {
                                JSONObject object=new JSONObject(res);
                                if(object.has("flag") && object.getInt("flag")==1)
                                    listener.onSuccess(res);
                                else
                                    listener.onSomethingWrong(object.getString("msg"));
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onSomethingWrong(e);
                        listener.onFailure(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void saveSteps(String url, StepsModel model,ResponseListener listener){
        Observable<ResponseBody> b=controller.storeSteps(url,model);
        b
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String res=responseBody.string();
                            if(res==null){
                                listener.onSomethingWrong(null);
                            }else {
                                JSONObject object=new JSONObject(res);
                                if(object.has("flag") && object.getInt("flag")==1)
                                    listener.onSuccess(res);
                                else
                                    listener.onSomethingWrong(object.getString("msg"));
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailure(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    public static final class NetworkBuilder {
            String token;
        public NetworkBuilder setHeader(){
            token= Prefs.getString(CommonMethod.TOKEN,null);
            return this;
        }

        public NetworkUtility build(){
            return new NetworkUtility(this);
        }
    }
}
