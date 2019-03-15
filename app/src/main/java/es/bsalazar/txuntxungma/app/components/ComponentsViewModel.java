package es.bsalazar.txuntxungma.app.components;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import es.bsalazar.txuntxungma.Injector;
import es.bsalazar.txuntxungma.app.base.BaseViewModel;
import es.bsalazar.txuntxungma.data.DataSource;
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse;
import es.bsalazar.txuntxungma.data.remote.FirestoreSource;
import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.entities.BaseError;
import es.bsalazar.txuntxungma.domain.entities.Component;
import es.bsalazar.txuntxungma.domain.entities.DataCallback;
import es.bsalazar.txuntxungma.domain.threading.UseCaseHandler;
import es.bsalazar.txuntxungma.domain.use_cases.GetLoginDataUseCase;
import es.bsalazar.txuntxungma.utils.ResultState;
import es.bsalazar.txuntxungma.utils.ShowState;

public class ComponentsViewModel extends BaseViewModel {

    private boolean initialize;
    private DataSource dataSource;

    private MutableLiveData<List<Component>> components = new MutableLiveData<List<Component>>() {
    };
    private MutableLiveData<FirebaseResponse<Component>> addComponentResponse = new MutableLiveData<FirebaseResponse<Component>>() {
    };
    private MutableLiveData<FirebaseResponse<Component>> removeComponentResponse = new MutableLiveData<FirebaseResponse<Component>>() {
    };
    private MutableLiveData<FirebaseResponse<Component>> modifyComponentResponse = new MutableLiveData<FirebaseResponse<Component>>() {
    };
    private MutableLiveData<ShowState> emptyList = new MutableLiveData<ShowState>() {
    };
    private MutableLiveData<Auth> auth = new MutableLiveData<Auth>() {
    };
    private MutableLiveData<Component> saveComponentResult = new MutableLiveData<Component>() {
    };
    private MutableLiveData<Component> updateComponentResult = new MutableLiveData<Component>() {
    };

    ComponentsViewModel(UseCaseHandler useCaseHandler) {
        super(useCaseHandler);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    void getComponentsFromDataSource() {
        initialize = true;
        loadingProgress.setValue(ShowState.SHOW);
        emptyList.setValue(ShowState.HIDE);
        dataSource.getComponents(new FirestoreSource.OnCollectionChangedListener<Component>() {
            @Override
            public void onCollectionChange(List<Component> collection) {
                if (initialize) {
                    loadingProgress.setValue(ShowState.HIDE);

                    if (collection.size() > 0)
                        components.setValue(collection);
                    else
                        emptyList.setValue(ShowState.SHOW);

                    initialize = false;
                }
            }

            @Override
            public void onDocumentAdded(int index, Component object) {
                if (!initialize) {
                    addComponentResponse.setValue(new FirebaseResponse<>(index, object));
                }
            }

            @Override
            public void onDocumentChanged(int index, Component object) {
                if (!initialize) {
                    modifyComponentResponse.setValue(new FirebaseResponse<>(index, object));
                }
            }

            @Override
            public void onDocumentRemoved(int index, Component object) {
                if (!initialize) {
                    removeComponentResponse.setValue(new FirebaseResponse<>(index, object));
                }
            }
        });
    }

    void saveComponent(String name){
        dataSource.saveComponent(new Component(name), component -> saveComponentResult.setValue(component));
    }

    void deleteComponent(String componentId){
        dataSource.deleteComponent(componentId);
    }


    void updateComponent(Component component){
        dataSource.updateComponent(component, document -> updateComponentResult.setValue(document));
    }

    void getAuthData() {
        dataSource.getLoginData(new DataCallback<Auth, BaseError>() {
            @Override
            public void onSuccess(Auth result) {
                auth.setValue(result);
            }

            @Override
            public void onFailure(BaseError errorBase) {
                auth.setValue(null);
            }
        });
    }

    public MutableLiveData<List<Component>> getComponents() {
        return components;
    }

    public MutableLiveData<FirebaseResponse<Component>> getAddComponentResponse() {
        return addComponentResponse;
    }

    public MutableLiveData<FirebaseResponse<Component>> getRemoveComponentResponse() {
        return removeComponentResponse;
    }

    public MutableLiveData<FirebaseResponse<Component>> getModifyComponentResponse() {
        return modifyComponentResponse;
    }

    public MutableLiveData<ShowState> getEmptyList() {
        return emptyList;
    }

    public MutableLiveData<Auth> getAuth() {
        return auth;
    }

    public MutableLiveData<Component> getSaveComponentResult() {
        return saveComponentResult;
    }

    public MutableLiveData<Component> getUpdateComponentResult() {
        return updateComponentResult;
    }
}
