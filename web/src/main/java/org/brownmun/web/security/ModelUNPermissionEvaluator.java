package org.brownmun.web.security;

import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.Position;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Delegate;
import org.brownmun.core.school.model.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.OptionalLong;

/**
 * Hooks into Spring Security's {@link PermissionEvaluator} abstraction. This lets us use things like
 * {@code hasPermission(#myCommittee, 'modify')} in {@link org.springframework.security.access.prepost.PreAuthorize}
 * annotations.
 */
@Service
public class ModelUNPermissionEvaluator implements PermissionEvaluator
{
    private static final String ACCESS_PERMISSION = "access";
    private static final String MODIFY_PERMISSION = "modify";

    private final SchoolService schoolService;
    private final CommitteeService committeeService;

    @Autowired
    public ModelUNPermissionEvaluator(SchoolService schoolService, CommitteeService committeeService)
    {
        this.schoolService = schoolService;
        this.committeeService = committeeService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission)
    {
        if (!(authentication.getPrincipal() instanceof User))
        {
            return false;
        }

        if (!ACCESS_PERMISSION.equals(permission) && !MODIFY_PERMISSION.equals(permission))
        {
            return false;
        }

        User user = (User) authentication.getPrincipal();

        if (targetDomainObject instanceof Committee)
        {
            if (ACCESS_PERMISSION.equals(permission))
            {
                return user.canAccessCommittee(((Committee) targetDomainObject).getId());
            }
            else if (MODIFY_PERMISSION.equals(permission))
            {
                return user.canModifyCommittee(((Committee) targetDomainObject).getId());
            }
        }
        else if (targetDomainObject instanceof School)
        {
            if (ACCESS_PERMISSION.equals(permission))
            {
                return user.canAccessSchool(((School) targetDomainObject).getId());
            }
            else if (MODIFY_PERMISSION.equals(permission))
            {
                return user.canModifySchool(((School) targetDomainObject).getId());
            }
        }
        else if (targetDomainObject instanceof Position)
        {
            if (ACCESS_PERMISSION.equals(permission))
            {
                return user.canAccessCommittee(((Position) targetDomainObject).getCommittee().getId());
            }
            else if (MODIFY_PERMISSION.equals(permission))
            {
                return user.canModifyCommittee(((Position) targetDomainObject).getCommittee().getId());
            }
        }
        else if (targetDomainObject instanceof Delegate)
        {
            if (ACCESS_PERMISSION.equals(permission))
            {
                return user.canAccessSchool(((Delegate) targetDomainObject).getSchool().getId());
            }
            else if (MODIFY_PERMISSION.equals(permission))
            {
                return user.canModifySchool(((Delegate) targetDomainObject).getSchool().getId());
            }
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission)
    {
        if (!(authentication.getPrincipal() instanceof User))
        {
            return false;
        }

        if (!ACCESS_PERMISSION.equals(permission) && !MODIFY_PERMISSION.equals(permission))
        {
            return false;
        }

        if (!(targetId instanceof Long))
        {
            return false;
        }

        long id = (long) targetId;
        User user = (User) authentication.getPrincipal();

        switch (targetType)
        {
            case "Committee":
                if (ACCESS_PERMISSION.equals(permission))
                {
                    return user.canAccessCommittee(id);
                }
                else if (MODIFY_PERMISSION.equals(permission))
                {
                    return user.canModifyCommittee(id);
                }
                break;
            case "School":
                if (ACCESS_PERMISSION.equals(permission))
                {
                    return user.canAccessSchool(id);
                }
                else if (MODIFY_PERMISSION.equals(permission))
                {
                    return user.canModifySchool(id);
                }
                break;
            case "Position":
                OptionalLong maybeCommittee = committeeService.findCommitteeId(id);
                if (maybeCommittee.isPresent())
                {
                    if (ACCESS_PERMISSION.equals(permission))
                    {
                        return user.canAccessCommittee(maybeCommittee.getAsLong());
                    }
                    else if (MODIFY_PERMISSION.equals(permission))
                    {
                        return user.canModifyCommittee(maybeCommittee.getAsLong());
                    }
                }
                return false;
            case "Delegate":
                OptionalLong maybeSchool = schoolService.findSchoolId(id);
                if (maybeSchool.isPresent())
                {
                    if (ACCESS_PERMISSION.equals(permission))
                    {
                        return user.canAccessSchool(maybeSchool.getAsLong());
                    }
                    else if (MODIFY_PERMISSION.equals(permission))
                    {
                        return user.canModifySchool(maybeSchool.getAsLong());
                    }
                }
                return false;
            default:
                return false;
        }

        return false;
    }
}
