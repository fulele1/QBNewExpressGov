package com.xaqb.qb_ec.icon;

import com.joanzapata.iconify.Icon;

/**
 * Created by lenovo on 2018/8/9.
 */

public enum  EcIcons implements Icon{
    icon_scan('\ue606'),
    icon_ali_pay('\ue606');

    EcIcons(char character) {
        this.character = character;
    }

    private char  character;
    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}
