package es.bsalazar.txuntxungma.domain.use_cases;

import es.bsalazar.txuntxungma.data.DataProvider;
import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.entities.BaseError;
import es.bsalazar.txuntxungma.domain.entities.DataCallback;
import es.bsalazar.txuntxungma.domain.threading.UseCase;


public class SaveLoginDataUseCase extends UseCase<SaveLoginDataUseCase.RequestValues, SaveLoginDataUseCase.ResponseValue> {

    private final DataProvider dataProvider;

    public SaveLoginDataUseCase(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        dataProvider.saveLoginData(requestValues.getAuthRequest(), new DataCallback<Boolean, BaseError>() {
            @Override
            public void onSuccess(Boolean result) {
                getUseCaseCallback().onSuccess(new ResponseValue());
            }

            @Override
            public void onFailure(BaseError errorBase) {
                getUseCaseCallback().onError(errorBase.getErrorMessage(), -1);
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final Auth authRequest;

        public RequestValues(Auth authRequest) {
            this.authRequest = authRequest;
        }

        Auth getAuthRequest() {
            return this.authRequest;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {}

}
