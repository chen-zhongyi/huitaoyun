package jobs;

import play.jobs.Every;
import play.jobs.OnApplicationStart;
import utils.QiniuUtils;

@Every("1h")
@OnApplicationStart
public class FreshQiniuTokenJob extends BaseJob {
    
    @Override
    public void doJob() throws Exception {
        QiniuUtils.initUpToken();
    }
    
}
