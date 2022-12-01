import PropTypes from "prop-types";
import { connect } from "react-redux";

const Alert = ({ alerts }) =>
  alerts !== null &&
  alerts.length > 0 &&
  (
    // <div key={alert.id} className={`alert alert-${alert.alertType}`}>
    //   {alert.msg}
    // </div>
      message.error('This is an error message')
    
  );

Alert.propTypes = {
  alerts: PropTypes.array.isRequired
};

const mapStateToProps = state => ({
  alerts: state.alert
});

export default connect(mapStateToProps)(Alert);
