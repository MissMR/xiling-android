package com.dodomall.ddmall.ddui.tools;

import com.dodomall.ddmall.shared.bean.Product;
import com.dodomall.ddmall.shared.bean.Property;
import com.dodomall.ddmall.shared.bean.PropertyValue;
import com.dodomall.ddmall.shared.bean.SkuPvIds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by Jigsaw at 2019/3/8
 * Sku 规格组合帮助类
 */
public class SkuHelper {


    private Product mProduct;
    // 已选择的规格值
    private Map<Property, PropertyValue> mSelectedPropertyValues;

    private OnSkuPropertySelectListener mOnSkuPropertySelectListener;

    public SkuHelper(Product product, OnSkuPropertySelectListener listener) {
        if (product == null) {
            DLog.e("product 不能为null");
            return;
        }
        setOnSkuPropertySelectListener(listener);
        mProduct = product;
        mSelectedPropertyValues = new HashMap<>(product.skus.size());
        init();

    }

    private void init() {

        try {
            if (mProduct.skus != null && mProduct.skus.size() == 1 && mProduct.skus.get(0).stock > 0) {
                // 只有一个sku 默认选中
                for (int i = 0; i < mProduct.properties.size(); i++) {
                    Property property = mProduct.properties.get(i);
                    setPropertySelected(property, property.values.get(0));
                    addSelectedPropertyValue(property, property.values.get(0));
                }

            } else if (mProduct.getSelectedSkuPvIds() != null) {
                initSelectedData();
            }
            this.calculate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSelectedData() {
        mSelectedPropertyValues.clear();
        String propertyValueIds = mProduct.getSelectedSkuPvIds().propertyValueIds;

        for (Property property : mProduct.properties) {
            for (PropertyValue value : property.values) {
                value.isSelected = propertyValueIds.contains(value.id);
                if (value.isSelected) {
                    mSelectedPropertyValues.put(property, value);
                }
            }
        }

    }

    public void reset() {
        mProduct.resetPropertyState();
        getSelectedPropertyValues().clear();
    }

    public void toggle(Property property, PropertyValue propertyValue) {
        propertyValue.isSelected = !propertyValue.isSelected;
        if (propertyValue.isSelected) {
            setPropertySelected(property, propertyValue);
            addSelectedPropertyValue(property, propertyValue);
        } else {
            clearPropertySelected(property);
            getSelectedPropertyValues().remove(property);
        }

        if (getOnSkuPropertySelectListener() != null) {
            getOnSkuPropertySelectListener().onPropertyChanged(getSelectedPropertyValues(), checkSelectedEnd());
        }

        calculate();

    }

    private void clearPropertySelected(Property property) {
        for (PropertyValue value : property.values) {
            value.setSelected(false);
        }
    }

    // 计算不能选择的规格属性
    public void calculate() {
        Map<Property, PropertyValue> selectedPropertyValues = getSelectedPropertyValues();
        List<Property> propertyList = mProduct.properties;
        if (selectedPropertyValues.isEmpty()) {
            reset();
        }

        List<String> ids;
        for (Property p : propertyList) {

            if (selectedPropertyValues.containsKey(p)) {
                // 已经选择的规格属性
                ids = getSelectedPropertyValueIdsExcludeThat(p);
                for (PropertyValue v : p.values) {
                    ids.add(v.id);
                    v.setEnable(checkPropertyValueIdsEnable(ids));
                    ids.remove(v.id);
                }
            } else {
                // 未选择的规格属性
                for (PropertyValue v : p.values) {
                    ids = getSelectedPropertyValueIdsAddThat(v);
                    v.setEnable(checkPropertyValueIdsEnable(ids));
                }
            }

        }

        if (getOnSkuPropertySelectListener() != null && checkSelectedEnd()) {
            getOnSkuPropertySelectListener().onSkuSelected(getSelectedPropertyValuesIds(), getSelectedSku());
        }

    }

    private void setPropertySelected(Property property, PropertyValue propertyValue) {
        for (PropertyValue v : property.values) {
            v.setSelected(v.id.equals(propertyValue.id));
        }
    }

    /**
     * 判断当前规格id集合是否可点
     *
     * @param idList
     * @return
     */
    private boolean checkPropertyValueIdsEnable(List<String> idList) {
        for (SkuPvIds s : mProduct.skus) {
            if (s.contains(idList) && s.stock > 0) {
                return true;
            }
        }
        return false;
    }

    // 是否所有的规格都已选择完毕
    private boolean checkSelectedEnd() {
        return mSelectedPropertyValues.keySet().size() == getProduct().properties.size();
    }

    private List<String> getSelectedPropertyValueIdsExcludeThat(Property property) {
        ArrayList<String> ids = new ArrayList<>();
        for (Property p : mSelectedPropertyValues.keySet()) {
            if (p != property) {
                ids.add(mSelectedPropertyValues.get(p).id);
            }
        }
        return ids;
    }

    /**
     * 当前规格值 和 已经选择的规格值进行组合 返回组合后的规格值id集合
     *
     * @param propertyValue 当前规格值
     * @return 组合后的规格值id集合
     */
    private List<String> getSelectedPropertyValueIdsAddThat(PropertyValue propertyValue) {
        ArrayList<String> ids = new ArrayList<>();
        for (Property p : mSelectedPropertyValues.keySet()) {
            ids.add(mSelectedPropertyValues.get(p).id);
        }
        ids.add(propertyValue.id);
        return ids;
    }

    private SkuPvIds getSelectedSku() {
        if (!checkSelectedEnd()) {
            DLog.e("!checkSelectedEnd()");
            return null;
        }
        for (SkuPvIds s : mProduct.skus) {
            if (s.contains(getSelectedPropertyValuesIds())) {
                return s;
            }
        }
        return null;
    }

    private List<String> getSelectedPropertyValuesIds() {
        ArrayList<String> ids = new ArrayList<>();
        for (PropertyValue value : mSelectedPropertyValues.values()) {
            ids.add(value.id);
        }
        return ids;
    }

    private String getSelectedPropertyValuesIdStr() {
        if (mSelectedPropertyValues.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (PropertyValue value : mSelectedPropertyValues.values()) {
            stringBuilder.append(value.id).append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }


    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product product) {
        mProduct = product;
    }

    public Map<Property, PropertyValue> getSelectedPropertyValues() {
        return mSelectedPropertyValues;
    }

    private void addSelectedPropertyValue(Property key, PropertyValue value) {
        getSelectedPropertyValues().put(key, value);
    }


    public OnSkuPropertySelectListener getOnSkuPropertySelectListener() {
        return mOnSkuPropertySelectListener;
    }

    public void setOnSkuPropertySelectListener(OnSkuPropertySelectListener onSkuPropertySelectListener) {
        mOnSkuPropertySelectListener = onSkuPropertySelectListener;
    }

    public interface OnSkuPropertySelectListener {
        void onSkuSelected(List<String> propertyValueIds, SkuPvIds skuPvIds);

        void onPropertyChanged(Map<Property, PropertyValue> propertyPropertyValueMap, boolean isSelectedEnd);
    }

}
