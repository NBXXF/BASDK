# ba合约代理api sdk 架构
1. aop 面向切面编程
2. 面向接口式编程 避免sdk 切换底层;如切换至其他平台的合约(目前是bh切换到ba),减少替换成本
# ba 合约http地址
1. https://binance-docs.github.io/apidocs/futures/cn/#249be3ac39
2. https://www.binance.com/cn/support/faq/c-4?navId=4

# 所有状态码  https://binance-docs.github.io/apidocs/testnet/cn/#cf68bca02a-2
-1000 UNKNOWN
An unknown error occured while processing the request.
处理请求时发生未知错误。
-1001 DISCONNECTED
Internal error; unable to process your request. Please try again.
内部错误; 无法处理您的请求。 请再试一次.
-1002 UNAUTHORIZED
You are not authorized to execute this request.
您无权执行此请求。
-1003 TOO_MANY_REQUESTS
Too many requests queued.
排队的请求过多。
Too many requests; please use the websocket for live updates.
请求权重过多； 请使用websocket获取最新更新。
Too many requests; current limit is %s requests per minute. Please use the websocket for live updates to avoid polling the API.
请求权重过多； 当前限制为每分钟％s请求权重。 请使用websocket进行实时更新，以避免轮询API。
Way too many requests; IP banned until %s. Please use the websocket for live updates to avoid bans.
请求权重过多； IP被禁止，直到％s。 请使用websocket进行实时更新，以免被禁。
-1004 DUPLICATE_IP
This IP is already on the white list
IP地址已经在白名单
-1005 NO_SUCH_IP
No such IP has been white listed
白名单上没有此IP地址
-1006 UNEXPECTED_RESP
An unexpected response was received from the message bus. Execution status unknown.
从消息总线收到意外的响应。执行状态未知。
-1007 TIMEOUT
Timeout waiting for response from backend server. Send status unknown; execution status unknown.
等待后端服务器响应超时。 发送状态未知； 执行状态未知。
-1014 UNKNOWN_ORDER_COMPOSITION
Unsupported order combination.
不支持当前的下单参数组合
-1015 TOO_MANY_ORDERS
Too many new orders.
新订单太多。
Too many new orders; current limit is %s orders per %s.  * 新订单太多； 当前限制为每％s ％s个订单。
-1016 SERVICE_SHUTTING_DOWN
This service is no longer available.
该服务不可用。
-1020 UNSUPPORTED_OPERATION
This operation is not supported.
不支持此操作。
-1021 INVALID_TIMESTAMP
Timestamp for this request is outside of the recvWindow.
此请求的时间戳在recvWindow之外。
Timestamp for this request was 1000ms ahead of the server's time.  * 此请求的时间戳比服务器时间提前1000毫秒。
-1022 INVALID_SIGNATURE
Signature for this request is not valid.
此请求的签名无效。
-1023 START_TIME_GREATER_THAN_END_TIME
Start time is greater than end time.
参数里面的开始时间在结束时间之后
11xx - Request issues
-1100 ILLEGAL_CHARS
Illegal characters found in a parameter.
在参数中发现非法字符。
Illegal characters found in parameter '%s'; legal range is '%s'.
在参数％s中发现非法字符； 合法范围是％s。
-1101 TOO_MANY_PARAMETERS
Too many parameters sent for this endpoint.
为此端点发送的参数太多。
Too many parameters; expected '%s' and received '%s'.
参数太多；预期为％s并收到了％s。
Duplicate values for a parameter detected.  * 检测到的参数值重复。
-1102 MANDATORY_PARAM_EMPTY_OR_MALFORMED
A mandatory parameter was not sent, was empty/null, or malformed.
未发送强制性参数，该参数为空/空或格式错误。
Mandatory parameter '%s' was not sent, was empty/null, or malformed.  * 强制参数％s未发送，为空/空或格式错误。
Param '%s' or '%s' must be sent, but both were empty/null!  * 必须发送参数％s或％s，但两者均为空！
-1103 UNKNOWN_PARAM
An unknown parameter was sent.
发送了未知参数。
-1104 UNREAD_PARAMETERS
Not all sent parameters were read.
并非所有发送的参数都被读取。
Not all sent parameters were read; read '%s' parameter(s) but was sent '%s'.
并非所有发送的参数都被读取； 读取了％s参数，但被发送了％s。
-1105 PARAM_EMPTY
A parameter was empty.
参数为空。
Parameter '%s' was empty.
参数％s为空。
-1106 PARAM_NOT_REQUIRED
A parameter was sent when not required.
发送了不需要的参数。
Parameter '%s' sent when not required.
发送了不需要参数％s。
-1111 BAD_PRECISION
Precision is over the maximum defined for this asset.
精度超过为此资产定义的最大值。
-1112 NO_DEPTH
No orders on book for symbol.
交易对没有挂单。
-1114 TIF_NOT_REQUIRED
TimeInForce parameter sent when not required.
发送的TimeInForce参数不需要。
-1115 INVALID_TIF
Invalid timeInForce.
无效的timeInForce
-1116 INVALID_ORDER_TYPE
Invalid orderType.
无效订单类型。
-1117 INVALID_SIDE
Invalid side.
无效买卖方向。
-1118 EMPTY_NEW_CL_ORD_ID
New client order ID was empty.
新的客户订单ID为空。
-1119 EMPTY_ORG_CL_ORD_ID
Original client order ID was empty.
客户自定义的订单ID为空。
-1120 BAD_INTERVAL
Invalid interval.
无效时间间隔。
-1121 BAD_SYMBOL
Invalid symbol.
无效的交易对。
-1125 INVALID_LISTEN_KEY
This listenKey does not exist.
此listenKey不存在。
-1127 MORE_THAN_XX_HOURS
Lookup interval is too big.
查询间隔太大。
More than %s hours between startTime and endTime.
从开始时间到结束时间之间超过％s小时。
-1128 OPTIONAL_PARAMS_BAD_COMBO
Combination of optional parameters invalid.
可选参数组合无效。
-1130 INVALID_PARAMETER
Invalid data sent for a parameter.
发送的参数为无效数据。
Data sent for paramter '%s' is not valid.
发送参数％s的数据无效。
20xx - Processing Issues
-2010 NEW_ORDER_REJECTED
NEW_ORDER_REJECTED
新订单被拒绝
-2011 CANCEL_REJECTED
CANCEL_REJECTED
取消订单被拒绝
-2013 NO_SUCH_ORDER
Order does not exist.
订单不存在。
-2014 BAD_API_KEY_FMT
API-key format invalid.
API-key 格式无效。
-2015 REJECTED_MBX_KEY
Invalid API-key, IP, or permissions for action.
无效的API密钥，IP或操作权限。
-2016 NO_TRADING_WINDOW
No trading window could be found for the symbol. Try ticker/24hrs instead.
找不到该交易对的交易窗口。 尝试改为24小时自动报价。
-2018 BALANCE_NOT_SUFFICIENT
Balance is insufficient.
余额不足
-2019 MARGIN_NOT_SUFFICIEN
Margin is insufficient.
杠杆账户余额不足
-2020 UNABLE_TO_FILL
Unable to fill.
无法成交
-2021 ORDER_WOULD_IMMEDIATELY_TRIGGER
Order would immediately trigger.
订单可能被立刻触发
-2022 REDUCE_ONLY_REJECT
ReduceOnly Order is rejected.
ReduceOnly订单被拒绝
-2023 USER_IN_LIQUIDATION
User in liquidation mode now.
用户正处于被强平模式
-2024 POSITION_NOT_SUFFICIENT
Position is not sufficient.
持仓不足
-2025 MAX_OPEN_ORDER_EXCEEDED
Reach max open order limit.
挂单量达到上限
-2026 REDUCE_ONLY_ORDER_TYPE_NOT_SUPPORTED
This OrderType is not supported when reduceOnly.
当前订单类型不支持reduceOnly
-2027 MAX_LEVERAGE_RATIO
Exceeded the maximum allowable position at current leverage.
挂单或持仓超出当前初始杠杆下的最大值
-2028 MIN_LEVERAGE_RATIO
Leverage is smaller than permitted: insufficient margin balance.
调整初始杠杆过低，导致可用余额不足
40xx - Filters and other Issues
-4000 INVALID_ORDER_STATUS
Invalid order status.
订单状态不正确
-4001 PRICE_LESS_THAN_ZERO
Price less than 0.
价格小于0
-4002 PRICE_GREATER_THAN_MAX_PRICE
Price greater than max price.
价格超过最大值
-4003 QTY_LESS_THAN_ZERO
Quantity less than zero.
数量小于0
-4004 QTY_LESS_THAN_MIN_QTY
Quantity less than min quantity.
数量小于最小值
-4005 QTY_GREATER_THAN_MAX_QTY
Quantity greater than max quantity.
数量大于最大值
-4006 STOP_PRICE_LESS_THAN_ZERO
Stop price less than zero.
触发价小于最小值
-4007 STOP_PRICE_GREATER_THAN_MAX_PRICE
Stop price greater than max price.
触发价大于最大值
-4008 TICK_SIZE_LESS_THAN_ZERO
Tick size less than zero.
价格精度小于0
-4009 MAX_PRICE_LESS_THAN_MIN_PRICE
Max price less than min price.
最大价格小于最小价格
-4010 MAX_QTY_LESS_THAN_MIN_QTY
Max qty less than min qty.
最大数量小于最小数量
-4011 STEP_SIZE_LESS_THAN_ZERO
Step size less than zero.
步进值小于0
-4012 MAX_NUM_ORDERS_LESS_THAN_ZERO
Max num orders less than zero.
最大订单量小于0
-4013 PRICE_LESS_THAN_MIN_PRICE
Price less than min price.
价格小于最小价格
-4014 PRICE_NOT_INCREASED_BY_TICK_SIZE
Price not increased by tick size.
价格增量不是价格精度的倍数。
-4015 INVALID_CL_ORD_ID_LEN
Client order id is not valid.
客户订单ID有误。
Client order id length should be less than 32 chars
客户订单ID长度应该少于32字符
-4016 PRICE_HIGHTER_THAN_MULTIPLIER_UP
Price is higher than mark price multiplier cap.
-4017 MULTIPLIER_UP_LESS_THAN_ZERO
Multiplier up less than zero.
价格上限小于0
-4018 MULTIPLIER_DOWN_LESS_THAN_ZERO
Multiplier down less than zero.
价格下限小于0
-4019 COMPOSITE_SCALE_OVERFLOW
Composite scale too large.
-4020 TARGET_STRATEGY_INVALID
Target strategy invalid for orderType '%s',reduceOnly '%b'.
目标策略值不适合%s订单状态, 只减仓%b。
-4021 INVALID_DEPTH_LIMIT
Invalid depth limit.
深度信息的limit值不正确。
'%s' is not valid depth limit.
%s不是合理的深度信息的limit值。
-4022 WRONG_MARKET_STATUS
market status sent is not valid.
发送的市场状态不正确。
-4023 QTY_NOT_INCREASED_BY_STEP_SIZE
Qty not increased by step size.
数量的递增值不是步进值的倍数。
-4024 PRICE_LOWER_THAN_MULTIPLIER_DOWN
Price is lower than mark price multiplier floor.
-4025 MULTIPLIER_DECIMAL_LESS_THAN_ZERO
Multiplier decimal less than zero.
-4026 COMMISSION_INVALID
Commission invalid.
收益值不正确
%s less than zero.
%s少于0
%s absolute value greater than %s
%s绝对值大于%s
-4027 INVALID_ACCOUNT_TYPE
Invalid account type.
账户类型不正确。
-4028 INVALID_LEVERAGE
Invalid leverage
杠杆倍数不正确
Leverage %s is not valid
杠杆%s不正确
Leverage %s already exist with %s
杠杆%s已经存在于%s
-4029 INVALID_TICK_SIZE_PRECISION
Tick size precision is invalid.
价格精度小数点位数不正确。
-4030 INVALID_STEP_SIZE_PRECISION
Step size precision is invalid.
步进值小数点位数不正确。
-4031 INVALID_WORKING_TYPE
Invalid parameter working type
不正确的参数类型
Invalid parameter working type: %s
不正确的参数类型: %s
-4032 EXCEED_MAX_CANCEL_ORDER_SIZE
Exceed maximum cancel order size.
超过可以取消的最大订单量。
Invalid parameter working type: %s
不正确的参数类型: %s
-4033 INSURANCE_ACCOUNT_NOT_FOUND
Insurance account not found.
风险保障基金账号没找到。
-4044 INVALID_BALANCE_TYPE
Balance Type is invalid.
余额类型不正确。
-4045 MAX_STOP_ORDER_EXCEEDED
Reach max stop order limit.
达到止损单的上限。
-4046 NO_NEED_TO_CHANGE_MARGIN_TYPE
No need to change margin type.
不需要切换仓位模式。
-4047 THERE_EXISTS_OPEN_ORDERS
Margin type cannot be changed if there exists open orders.
如果有挂单，仓位模式不能切换。
-4048 THERE_EXISTS_QUANTITY
Margin type cannot be changed if there exists position.
如果有仓位，仓位模式不能切换。
-4049 ADD_ISOLATED_MARGIN_REJECT
Add margin only support for isolated position.
-4050 CROSS_BALANCE_INSUFFICIENT
Cross balance insufficient.
全仓余额不足。
-4051 ISOLATED_BALANCE_INSUFFICIENT
Isolated balance insufficient.
逐仓余额不足。
-4052 NO_NEED_TO_CHANGE_AUTO_ADD_MARGIN
No need to change auto add margin.
-4053 AUTO_ADD_CROSSED_MARGIN_REJECT
Auto add margin only support for isolated position.
自动增加保证金只适用于逐仓。
-4054 ADD_ISOLATED_MARGIN_NO_POSITION_REJECT
Cannot add position margin: position is 0.
不能增加逐仓保证金: 持仓为0
-4055 AMOUNT_MUST_BE_POSITIVE
Amount must be positive.
数量必须是正整数
-4056 INVALID_API_KEY_TYPE
Invalid api key type.
API key的类型不正确
-4057 INVALID_RSA_PUBLIC_KEY
Invalid api public key
API key不正确
-4058 MAX_PRICE_TOO_LARGE
maxPrice and priceDecimal too large,please check.
maxPrice和priceDecimal太大，请检查。
-4059 NO_NEED_TO_CHANGE_POSITION_SIDE
No need to change position side.
无需变更仓位方向
-4060 INVALID_POSITION_SIDE
Invalid position side.
仓位方向不正确。
-4061 POSITION_SIDE_NOT_MATCH
Order's position side does not match user's setting.
订单的持仓方向和用户设置不一致。
-4062 REDUCE_ONLY_CONFLICT
Invalid or improper reduceOnly value.
仅减仓的设置不正确。
-4063 INVALID_OPTIONS_REQUEST_TYPE
Invalid options request type
无效的期权请求类型
-4064 INVALID_OPTIONS_TIME_FRAME
Invalid options time frame
无效的期权时间窗口
-4065 INVALID_OPTIONS_AMOUNT
Invalid options amount
无效的期权数量
-4066 INVALID_OPTIONS_EVENT_TYPE
Invalid options event type
无效的期权事件类型
-4067 POSITION_SIDE_CHANGE_EXISTS_OPEN_ORDERS
Position side cannot be changed if there exists open orders.
如果有挂单，无法修改仓位方向。
-4068 POSITION_SIDE_CHANGE_EXISTS_QUANTITY
Position side cannot be changed if there exists position.
如果有仓位, 无法修改仓位方向。
-4069 INVALID_OPTIONS_PREMIUM_FEE
Invalid options premium fee
无效的期权费
-4070 INVALID_CL_OPTIONS_ID_LEN
Client options id is not valid.
客户的期权ID不合法
Client options id length should be less than 32 chars
客户的期权ID长度应该小于32个字符
-4071 INVALID_OPTIONS_DIRECTION
Invalid options direction
期权的方向无效
-4072 OPTIONS_PREMIUM_NOT_UPDATE
premium fee is not updated, reject order
期权费没有更新
-4073 OPTIONS_PREMIUM_INPUT_LESS_THAN_ZERO
input premium fee is less than 0, reject order
输入的期权费小于0
-4074 OPTIONS_AMOUNT_BIGGER_THAN_UPPER
Order amount is bigger than upper boundary or less than 0, reject order
-4075 OPTIONS_PREMIUM_OUTPUT_ZERO
output premium fee is less than 0, reject order
-4076 OPTIONS_PREMIUM_TOO_DIFF
original fee is too much higher than last fee
期权的费用比之前的费用高
-4077 OPTIONS_PREMIUM_REACH_LIMIT
place order amount has reached to limit, reject order
下单的数量达到上限
-4078 OPTIONS_COMMON_ERROR
options internal error
期权内部系统错误
-4079 INVALID_OPTIONS_ID
invalid options id
invalid options id: %s
duplicate options id %d for user %d
期权ID无效
-4080 OPTIONS_USER_NOT_FOUND
user not found
user not found with id: %s
用户找不到
-4081 OPTIONS_NOT_FOUND
options not found
options not found with id: %s
期权找不到
-4082 INVALID_BATCH_PLACE_ORDER_SIZE
Invalid number of batch place orders.
Invalid number of batch place orders: %s
批量下单的数量不正确
-4083 PLACE_BATCH_ORDERS_FAIL
Fail to place batch orders.
无法批量下单
-4084 UPCOMING_METHOD
Method is not allowed currently. Upcoming soon.
方法不支持
-4085 INVALID_NOTIONAL_LIMIT_COEF
Invalid notional limit coefficient
期权的有限系数不正确
-4086 INVALID_PRICE_SPREAD_THRESHOLD
Invalid price spread threshold
无效的价差阀值
-4087 REDUCE_ONLY_ORDER_PERMISSION
User can only place reduce only order
用户只能下仅减仓订单
-4088 NO_PLACE_ORDER_PERMISSION
User can not place order currently
用户当前不能下单
-4104 INVALID_CONTRACT_TYPE
Invalid contract type
无效的合约类型
-4114 INVALID_CLIENT_TRAN_ID_LEN
clientTranId is not valid
clientTranId不正确
Client tran id length should be less than 64 chars
客户的tranId长度应该小于64个字符
-4115 DUPLICATED_CLIENT_TRAN_ID
clientTranId is duplicated
clientTranId重复
Client tran id should be unique within 7 days
客户的tranId应在7天内唯一
-4118 REDUCE_ONLY_MARGIN_CHECK_FAILED
ReduceOnly Order Failed. Please check your existing position and open orders
仅减仓订单失败。请检查现有的持仓和挂单
-4131 MARKET_ORDER_REJECT
The counterparty's best price does not meet the PERCENT_PRICE filter limit
交易对手的最高价格未达到PERCENT_PRICE过滤器限制
-4135 INVALID_ACTIVATION_PRICE
Invalid activation price
无效的激活价格
-4141 SYMBOL_ALREADY_CLOSED
Symbol is closed
交易对已下架
