-- 本文件中的数据，会被自动导入数据库，如果字段对应错误，会导入不成功

-- user : 真正初始化时，password 要加密
insert into base_user(user_name,login_name,password,create_time) values ('jiang','jiang','pass',sysdate())

-- 初始化跟节点
insert into base_menu(name,code,create_time) values ('root','root',sysdate())
-- role
insert into base_role(name,create_time) values ('admin',sysdate())
insert into base_role(name,create_time) values ('user',sysdate())