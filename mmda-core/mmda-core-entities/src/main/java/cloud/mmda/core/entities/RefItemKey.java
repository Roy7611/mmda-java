package cloud.mmda.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * 引用项次键
 */
@NoArgsConstructor
@AllArgsConstructor
public class RefItemKey extends RefKey {
    public RefItemKey(final String refName, long refID, int refItemID) {
        super(refName, refID);
        this.refItemID = refItemID;
    }
    /**
     * 序号
     */
    @NotNull
    @Getter @Setter
    private int refItemID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefItemKey that)) return false;
        if (refItemID != that.refItemID) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), refItemID);
    }

    @Override
    public String toString() {
        return join(refName,refID,refItemID);
    }

    @Override
    public final void parse(String s){
        Objects.requireNonNull(s);
        String[] ks = s.split(CompositeKey.KEY_DELIMITER);
        this.refName = ks[0];
        this.refID = Long.parseLong(ks[1]);
        this.refItemID = Integer.parseInt(ks[2]);
    }

    public static RefItemKey valueOf(String s){
        Objects.requireNonNull(s);
        String[] ks = s.split(CompositeKey.KEY_DELIMITER);
        return new RefItemKey(ks[0],Long.parseLong(ks[1]),Integer.parseInt(ks[2]));
    }
}
