package drawing;

import java.util.EventListener;

public interface DrawActionListener extends EventListener{

	public void actionDataChanged(ActionDataChangedEvent event);
}
