package cloud.mmda.core.entities;

import cloud.mmda.core.models.FlowTrail;

import java.util.List;

/**
 * 支持流程追踪的实体接口
 */
public interface Flowable<S extends Enum<S>> extends Ownable {
    /**
     * 获取状态
     * @return
     */
    Enum<S> getStatus();
    /**
     * 获取流程追踪
     * @return
     */
    List<FlowTrail> getFlowTrails();

    /**
     * 设置流程追踪
     * @param flowTrails
     */
    void setFlowTrails(List<FlowTrail> flowTrails);
}
