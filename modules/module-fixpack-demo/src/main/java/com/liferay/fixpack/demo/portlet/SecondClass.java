	/*package com.liferay.fixpack.demo.portlet;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true, service = SecondClass.class)
public class SecondClass {

	@Activate
	public void start(BundleContext bc){
		//_mfdp.listUsers(this.getClass().getName());
	}

	@Reference
    protected void setModuleFixpackDemoPortlet(ModuleFixpackDemoPortlet service) {
		_mfdp = service;
    }
	
	private ModuleFixpackDemoPortlet _mfdp;

}
*/