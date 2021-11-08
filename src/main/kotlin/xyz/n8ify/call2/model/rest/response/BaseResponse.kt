package xyz.n8ify.call2.model.rest.response

class BaseResponse<T>(val success: Boolean, val additionalData : T? = null)