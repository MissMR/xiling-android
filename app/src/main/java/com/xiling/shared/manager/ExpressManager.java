package com.xiling.shared.manager;

import android.content.Context;

import com.xiling.shared.bean.ExpressCompany;
import com.xiling.shared.service.contract.IExpressService;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/6.
 */
public class ExpressManager {

    public static void checkExpress(Context context, String expressCode, String companyCode) {
        IExpressService service = ServiceManager.getInstance().createService(IExpressService.class);
        service.getExpressDetails(
                companyCode,
                expressCode,
                "1",
                System.currentTimeMillis()+""
        );

        service.listExpressCompany("").flatMap(new Function<List<ExpressCompany>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<ExpressCompany> expressCompanies) throws Exception {
                return null;
            }
        });
    }
}
