package es.bsalazar.txuntxungma.domain.use_cases;

import es.bsalazar.txuntxungma.data.DataProvider;
import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.entities.BaseError;
import es.bsalazar.txuntxungma.domain.entities.DataCallback;
import es.bsalazar.txuntxungma.domain.threading.UseCase;


public class GetLoginDataUseCase extends UseCase<GetLoginDataUseCase.RequestValues, GetLoginDataUseCase.ResponseValue> {

    private final DataProvider dataProvider;

    public GetLoginDataUseCase(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        dataProvider.getLoginData(new DataCallback<Auth, BaseError>() {
            @Override
            public void onSuccess(Auth result) {
                getUseCaseCallback().onSuccess(new ResponseValue(result));
            }

            @Override
            public void onFailure(BaseError errorBase) {
                getUseCaseCallback().onError(errorBase.getErrorMessage(), -1);
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {}

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final Auth authRequest;

        public ResponseValue(Auth authRequest) {
            this.authRequest = authRequest;
        }

        Auth getAuthRequest() {
            return this.authRequest;
        }
    }

}
