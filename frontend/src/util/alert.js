import { message } from 'antd';

export const alertError = (content, className, duration=3) => {
  message.error({
    duration,
    content,
    className
  })
}

export const alertSuccess = (content, className, duration=3) => {
  message.success({
    duration,
    content,
    className
  })
}