package online.muydinov.securedoc.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import online.muydinov.securedoc.entity.UserEntity;
import online.muydinov.securedoc.enumeration.EventType;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    private UserEntity user;
    private EventType type;
    private Map<?, ?> data;
}
