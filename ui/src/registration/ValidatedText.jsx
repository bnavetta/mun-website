import React from "react";
import { Field } from "react-form";

export default function ValidatedText(props) {
    const { onChange, onBlur, field, validate, ...rest } = props;

    return (
        <Field validate={validate} field={field}>
            { fieldApi => {
                const { value, error, warning, success, setValue, setTouched, setError } = fieldApi;
                return (
                    <React.Fragment>
                        <input
                            {...rest}
                            value={value || ''}
                            onChange={e => {
                                setValue(e.target.value);
                                if (onChange) {
                                    onChange(e.target.value, e);
                                }
                            }}
                            onBlur={e => {
                                setTouched();
                                setError(null);
                                if (onBlur) {
                                    onBlur(e);
                                }
                            }}
                        />
                        { error ? <p className="form-error">{ error }</p> : null }
                        { !error && warning ? <p className="form-warning">{ warning }</p> : null }
                    </React.Fragment>
                );
            } }
        </Field>
    );
}