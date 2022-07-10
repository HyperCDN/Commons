package de.hypercdn.commons.api.execution.interceptor;

import de.hypercdn.commons.api.execution.action.ExecutionAction;
import de.hypercdn.commons.imp.execution.misc.exception.ExecutionException;

public interface ExecutionInterceptor{

	Object intercept(Chain chain);

	interface Chain{

		ExecutionAction<?, ?> action();

		Object lastIn();

		Object lastOut();

		Object execute(Object object) throws ExecutionException;

		interface Internal extends Chain{

			void setLast(Object in, Object out);

		}

	}

}
