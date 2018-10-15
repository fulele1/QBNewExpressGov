package com.xaqb.qb_core.app;

import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fule on 2018/6/26.
 */

public class Configurator  {
    private static final HashMap<String ,Object> LATTE_CONFIGS = new HashMap<>();
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();
    private Configurator(){
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY.name(),false);
    }

    public static Configurator getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final Configurator INSTANCE = new Configurator();
    }

    final HashMap<String ,Object> getLatteConfigs(){
        return LATTE_CONFIGS;
    }

    public final void  configure(){
        initIcons();//因为是在项目中为通用，所以在configure中直接加入。
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY.name(),true);
    }

    public  final Configurator withApiHost(String host){
        LATTE_CONFIGS.put(ConfigType.API_HOST.name(),host);
        return this;
    }

    //初始化字体样式
    private void initIcons(){
        if (ICONS.size()>0){
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
                for (int i = 0;i<ICONS.size();i++){
                    initializer.with(ICONS.get(i));
                }
        }
    }


    //加入自己的字体图标
    public final Configurator withIcon(IconFontDescriptor descriptor){
        ICONS.add(descriptor);
        return this;
    }



    private  void  checkConfiguration(){
        final  boolean isReady = (boolean) LATTE_CONFIGS.get(ConfigType.CONFIG_READY.name());
        if (!isReady){
            throw  new RuntimeException("Configuration is not ready,call configure");
        }
    }

    final <T> T getConfiguration(Enum<ConfigType> key){
        checkConfiguration();
        return (T) LATTE_CONFIGS.get(key.name());
    }
}











