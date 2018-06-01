package com.example.testmvp.splash;

public class SplashPresenterImpl implements SplashPresenter,SplashCommunicator{

    private SplashView splashView;
    private SplashCommunicatorImpl _splashInteractor;

    public SplashPresenterImpl(SplashView loginView, SplashCommunicatorImpl splashInteractor) {
        this.splashView = loginView;
        this._splashInteractor=splashInteractor;
    }

    @Override
    public void onDestroy() {
        splashView = null;
    }

    @Override
    public void viewCreated() {
        //splashView.showProgress();
        splashView.showAnimation();
        _splashInteractor.openSpalshScreen(this, "zoom");
    }


    @Override
    public void openSpalshScreen(SplashCommunicator listener, String key) {
        if (splashView != null) {
            if(key.equals("zoom")){
                splashView.showZoomAnim();
                _splashInteractor.openSpalshScreen(this, "home");
            }else{
                splashView.navigateToHome();
            }
        }
    }
}
