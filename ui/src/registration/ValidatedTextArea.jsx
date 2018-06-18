import React from "react";
import { Field } from "react-form";

export default function ValidatedTextArea(props) {
    const { onChange, onBlur, field, validate, ...rest } = props;

    return (
        <Field validate={validate} field={field}>
            {fieldApi => {
                const {
                    value,
                    error,
                    warning,
                    success,
                    setValue,
                    setTouched,
                } = fieldApi;
                return (
                    <React.Fragment>
                        <textarea
                            {...rest}
                            value={value || ""}
                            onChange={e => {
                                setValue(e.target.value);
                                if (onChange) {
                                    onChange(e.target.value, e);
                                }
                            }}
                            onBlur={e => {
                                setTouched();
                                if (onBlur) {
                                    onBlur(e);
                                }
                            }}
                        />
                        {error ? <p className="form-error">{error}</p> : null}
                        {!error && warning ? (
                            <p className="form-warning">{warning}</p>
                        ) : null}
                    </React.Fragment>
                );
            }}
        </Field>
    );
}
