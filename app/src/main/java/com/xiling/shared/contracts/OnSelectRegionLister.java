package com.xiling.shared.contracts;

import java.util.HashMap;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.contracts
 * @since 2017-07-06
 */
public interface OnSelectRegionLister {
    void selected(HashMap<String, IRegion> regions);
}
