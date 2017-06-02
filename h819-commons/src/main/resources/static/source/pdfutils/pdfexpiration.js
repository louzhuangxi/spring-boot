function CheckExpire() {
	var nowDate = new Date();
	var startDateStr = "2011-01-01";
	var startDate = new Date(startDateStr);
	var expiredDays = 365;
	var alertDays = 355;
	var nButton;

	//现在时间减去开始时间，得到已经进行的时间
	var DiffDays = ((nowDate - startDate) / (1000 * 60 * 60 * 24));
	
	//((nowDay >= alertDays) && (nowDay <= expiredDays)) {
	
	//到了警告期，还没有过期
	if ( alertDays <=DiffDays && DiffDays<expiredDays) {
		nButton = app.alert("文件即将超出使用日期！\n\n The expiration date is drawing near!");
		// 修改 nButtton 的默认返回值，不关闭文件
		nButton = 0;
	}
	//过期
	if (DiffDays>=expiredDays) {
		nButton = app.alert("文件超出使用日期！\n\n The expiration date has already passed !");

	}
	//nType =0 时, nButtton 的默认返回值是 1
	if (nButton == 1)//关闭文件
		this.closeDoc();
}

CheckExpire();