package es.bsalazar.txuntxungma.domain.use_cases;

import es.bsalazar.txuntxungma.data.DataProvider;
import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.entities.BaseError;
import es.bsalazar.txuntxungma.domain.entities.DataCallback;
import es.bsalazar.txuntxungma.domain.threading.UseCase;


public class RemoveLoginDataUseCase extends UseCase<RemoveLoginDataUseCase.RequestValues, RemoveLoginDataUseCase.ResponseValue> {

    private final DataProvider dataProvider;

    public RemoveLoginDataUseCase(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        dataProvider.removeLoginData(new DataCallback<Boolean, BaseError>() {
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

    public static final class RequestValues implements UseCase.RequestValues {}

    public static final class ResponseValue implements UseCase.ResponseValue {}

}
