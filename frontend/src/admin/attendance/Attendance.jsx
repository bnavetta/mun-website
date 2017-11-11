import React from 'react';

import './Attendance.scss';

function Present({ attendance, section, onChange }) {
    const checked = attendance[section];
    const handleChange = e => onChange({ ...attendance, [section]: e.target.checked });
    return (
        <div className="Attendance-entry">
            <input type="checkbox"
                   value={section}
                   checked={checked}
                   onChange={handleChange}
            />
        </div>
    );
}

export default function Attendance({ attendance, onChange }) {
    return (
        <tr>
            <th>{`${attendance.positionName} (${attendance.schoolName})`}</th>
            <td><Present attendance={attendance} section='positionPaper' onChange={onChange} /></td>
            <td><Present attendance={attendance} section='sessionOne' onChange={onChange} /></td>
            <td><Present attendance={attendance} section='sessionTwo' onChange={onChange}/></td>
            <td><Present attendance={attendance} section='sessionThree' onChange={onChange}/></td>
            <td><Present attendance={attendance} section='sessionFour' onChange={onChange}/></td>
        </tr>
    );
}