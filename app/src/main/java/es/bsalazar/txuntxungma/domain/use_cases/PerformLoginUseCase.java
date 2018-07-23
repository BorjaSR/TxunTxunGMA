package es.bsalazar.txuntxungma.domain.use_cases;

import es.bsalazar.txuntxungma.data.DataProvider;
import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.entities.BaseError;
import es.bsalazar.txuntxungma.domain.entities.DataCallback;
import es.bsalazar.txuntxungma.domain.requests.AuthRequest;
import es.bsalazar.txuntxungma.domain.threading.UseCase;


public class PerformLoginUseCase extends UseCase<PerformLoginUseCase.RequestValues, PerformLoginUseCase.ResponseValue> {

    private final DataProvider dataProvider;

    public PerformLoginUseCase(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        dataProvider.getAuth(requestValues.getAuthRequest(), new DataCallback<String, BaseError>() {
            @Override
            public void onSuccess(String result) {
                getUseCaseCallback().onSuccess(new ResponseValue(result.equals(requestValues.getAuthRequest().getEncryptPass())));
            }

            @Override
            public void onFailure(BaseError errorBase) {
                getUseCaseCallback().onError(errorBase.getErrorMessage(), -1);
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final AuthRequest authRequest;

        public RequestValues(AuthRequest authRequest) {
            this.authRequest = authRequest;
        }

        AuthRequest getAuthRequest() {
            return this.authRequest;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final Boolean loginSuccess;

        ResponseValue(Boolean loginSuccess) {
            this.loginSuccess = loginSuccess;
        }

        public Boolean getLoginSuccess() {
            return this.loginSuccess;
        }
    }


}
