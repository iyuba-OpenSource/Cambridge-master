package tv.lycam.gift.entity.mqtt;

/**
 * Created by lycamandroid on 16/6/27.
 */
public class UserJoin {

    private String type;
    private MetaInfo metaInfo;

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
