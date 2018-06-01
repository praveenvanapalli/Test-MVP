package com.example.testmvp.detail;

public class DetailsPresenterImpl implements DetailsPresenter,DetailsCommunicator {

    private DetailsView detailsView;
    private DetailsCommunicatorImpl _splashInteractor;

    public DetailsPresenterImpl(DetailsView loginView, DetailsCommunicatorImpl splashInteractor) {
        this.detailsView = loginView;
        this._splashInteractor=splashInteractor;
    }

    @Override
    public void onDestroy() {
        detailsView = null;
    }

    @Override
    public void viewCreated() {
        _splashInteractor.openDetailsScreen(this);
    }


    @Override
    public void openDetailsScreen(DetailsCommunicator listener) {
        if (detailsView != null) {
            detailsView.navigateToHome();
        }
    }
}
